package com.zinc.waver.ui.presentation.screen.billing

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClient.BillingResponseCode
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ConsumeParams
import com.android.billingclient.api.ConsumeResponseListener
import com.android.billingclient.api.PendingPurchasesParams
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import com.zinc.waver.ui.presentation.model.WaverPlusType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class ChooseSubscription(
    private val activity: Activity,
    private val isForPurchase: Boolean,
    private val subsDone: () -> Unit,
    private val alreadyPurchased: (Boolean) -> Unit
) {
    private val _subscriptions = MutableStateFlow<List<String>>(emptyList())

    private val purchaseUpdateListener = PurchasesUpdatedListener { result, purchases ->
        if (result.responseCode == BillingResponseCode.OK && purchases != null) {
            for (purchase in purchases) {
                handlePurchase(purchase)
            }
        } else if (result.responseCode == BillingResponseCode.USER_CANCELED) {
            // User canceled the purchase
            Toast.makeText(activity, "USER_CANCELED", Toast.LENGTH_SHORT)
        } else {
            // Handle other error cases
            Toast.makeText(activity, "result : ${result.responseCode}", Toast.LENGTH_SHORT)
        }
    }

    private lateinit var billingClient: BillingClient

    fun billingSetup(waverPlusType: WaverPlusType) {
        val subType = "waver_plus"
        val planId = if (waverPlusType == WaverPlusType.YEAR) "per-year" else "per-month"
        billingClient = BillingClient.newBuilder(activity)
            .setListener(purchaseUpdateListener)
            .enablePendingPurchases(
                PendingPurchasesParams.newBuilder()
                    .enableOneTimeProducts()
                    .enablePrepaidPlans().build()
            )
            .build()

        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(result: BillingResult) {
                if (result.responseCode == BillingResponseCode.OK) {
                    checkSubscriptionStatus(subType, planId)
                    Log.e("ayhan", "billingSetup1 : ${result.responseCode}, ${result.debugMessage}")
                } else {
                    Log.e("ayhan", "billingSetup2 : ${result.responseCode}, ${result.debugMessage}")
                }
            }

            override fun onBillingServiceDisconnected() {
                // Handle billing service disconnection
                Log.e("ayhan", "billingSetup : onBillingServiceDisconnected")
            }
        })
    }

    fun checkSubscriptionStatus(
        subscriptionPlanId: String,
        planId: String
    ) {
        val queryPurchaseParams = QueryPurchasesParams.newBuilder()
            .setProductType(BillingClient.ProductType.SUBS)
            .build()

        billingClient.queryPurchasesAsync(
            queryPurchaseParams
        ) { result, purchases ->
            if (purchases.isEmpty()) {
                alreadyPurchased(false)
            }

            when (result.responseCode) {
                BillingResponseCode.OK -> {
                    for (purchase in purchases) {
                        Log.e("ayhan", "queryPurchasesAsync : purchase :$purchase")

                        if (purchase.products.contains(subscriptionPlanId) && purchase.isAcknowledged) {
                            _subscriptions.update {
                                val newList = it.toMutableList()
                                newList.addAll(purchase.products)
                                newList
                            }

                            alreadyPurchased(true)
                            return@queryPurchasesAsync
                        }
                    }
                }

                BillingResponseCode.USER_CANCELED -> {
                    // User canceled the purchase
                    Log.e("ayhan", "BillingResponseCode : USER_CANCELED")
                }

                else -> {
                    // Handle other error cases
                    Log.e("ayhan", "BillingResponseCode else : ${result.responseCode}")
                }
            }
            Log.e("ayhan", "::: $result, $purchases")

            // User does not have an active subscription
            if (isForPurchase) {
                querySubscriptionPlans(subscriptionPlanId, planId)
            }
        }
    }

    private fun querySubscriptionPlans(
        subscriptionPlanId: String,
        planId: String
    ) {

        val queryProductDetailsParams =
            QueryProductDetailsParams.newBuilder().setProductList(
                listOf(
                    QueryProductDetailsParams.Product.newBuilder()
                        .setProductId(subscriptionPlanId)
                        .setProductType(BillingClient.ProductType.SUBS)
                        .build()
                )
            ).build()

        Log.e("ayhan", "queryProductDetailsParams  :$queryProductDetailsParams")

        billingClient.queryProductDetailsAsync(queryProductDetailsParams) { billingResult, queryProductDetailsResult ->
            if (billingResult.responseCode == BillingResponseCode.OK) {
                var offerToken = ""
                val productDetails =
                    queryProductDetailsResult.productDetailsList.firstOrNull { product ->
                        product.subscriptionOfferDetails?.any {
                            Log.e(
                                "ayhan",
                                "subscriptionOfferDetails ${it.basePlanId}. $subscriptionPlanId"
                            )
                            if (it.basePlanId == planId) {
                                offerToken = it.offerToken
                                true
                            } else {
                                false
                            }
                        } == true
                    }
                Log.e("ayhan", "productDetails  :$productDetails")

                productDetails?.let {
                    val productDetailsParamsList = listOf(
                        BillingFlowParams.ProductDetailsParams.newBuilder()
                            .setProductDetails(it)
                            .setOfferToken(offerToken)
                            .build()
                    )

                    val billingFlowParams = BillingFlowParams.newBuilder()
                        .setProductDetailsParamsList(productDetailsParamsList)
                        .build()

                    billingClient.launchBillingFlow(activity, billingFlowParams)
                }
            } else {
                Log.e("ayhan", "querySubscriptionPlans  :$${billingResult.responseCode}")
            }

            queryProductDetailsResult.unfetchedProductList.forEach { product ->
                Log.e("ayhan", "Unfetched product: $product")
            }
        }
    }

//        val queryProductDetailsParams =
//            QueryProductDetailsParams.newBuilder()
//                .setProductList(
//                    listOf(
//                        QueryProductDetailsParams.Product.newBuilder()
//                            .setProductId(subscriptionPlanId)
//                            .setProductType(BillingClient.ProductType.SUBS)
//                            .build(),
//                    )
//                )
//                .build()


    private fun handlePurchase(purchase: Purchase) {
        val consumeParams = ConsumeParams.newBuilder()
            .setPurchaseToken(purchase.purchaseToken)
            .build()

        val listener = ConsumeResponseListener { billingResult, s ->
            Log.e("ayhan", "listener $billingResult")
        }

        billingClient.consumeAsync(consumeParams, listener)

        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            if (!purchase.isAcknowledged) {
                val acknowledgePurchaseParams = AcknowledgePurchaseParams
                    .newBuilder()
                    .setPurchaseToken(purchase.purchaseToken)
                    .build()

                billingClient.acknowledgePurchase(acknowledgePurchaseParams) { billingResult ->
                    if (billingResult.responseCode == BillingResponseCode.OK) {
                        _subscriptions.update {
                            val newList = it.toMutableList()
                            newList.addAll(purchase.products)
                            newList
                        }

                        subsDone.invoke()
                    }
                }
            } else {
                Log.e("ayhan", "isAcknowledged")
            }
        } else {
            Log.e("ayhan", "purchase.purchaseState : ${purchase.purchaseState}")
        }
    }
}