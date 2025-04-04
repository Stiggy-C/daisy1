# DefaultApi

All URIs are relative to */*

Method | HTTP request | Description
------------- | ------------- | -------------
[**actionsChainIdDelete**](DefaultApi.md#actionsChainIdDelete) | **DELETE** /actions-chain/{id} | 
[**actionsChainIdGet**](DefaultApi.md#actionsChainIdGet) | **GET** /actions-chain/{id} | 
[**actionsChainIdHead**](DefaultApi.md#actionsChainIdHead) | **HEAD** /actions-chain/{id} | 
[**actionsIdDelete**](DefaultApi.md#actionsIdDelete) | **DELETE** /actions/{id} | 
[**actionsIdGet**](DefaultApi.md#actionsIdGet) | **GET** /actions/{id} | 
[**actionsIdHead**](DefaultApi.md#actionsIdHead) | **HEAD** /actions/{id} | 
[**postAction**](DefaultApi.md#postAction) | **POST** /actions | 
[**postActionsChain**](DefaultApi.md#postActionsChain) | **POST** /actions-chain | 

<a name="actionsChainIdDelete"></a>
# **actionsChainIdDelete**
> actionsChainIdDelete(id)



### Example
```java
// Import classes:
//import io.openenterprise.daisy.rs.ApiException;
//import io.openenterprise.daisy.rs.model.DefaultApi;


DefaultApi apiInstance = new DefaultApi();
UUID id = new UUID(); // UUID | Entity ID
try {
    apiInstance.actionsChainIdDelete(id);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#actionsChainIdDelete");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | [**UUID**](.md)| Entity ID |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

<a name="actionsChainIdGet"></a>
# **actionsChainIdGet**
> Action actionsChainIdGet(id)



### Example
```java
// Import classes:
//import io.openenterprise.daisy.rs.ApiException;
//import io.openenterprise.daisy.rs.model.DefaultApi;


DefaultApi apiInstance = new DefaultApi();
UUID id = new UUID(); // UUID | Entity ID
try {
    Action result = apiInstance.actionsChainIdGet(id);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#actionsChainIdGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | [**UUID**](.md)| Entity ID |

### Return type

[**Action**](Action.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="actionsChainIdHead"></a>
# **actionsChainIdHead**
> actionsChainIdHead(id)



### Example
```java
// Import classes:
//import io.openenterprise.daisy.rs.ApiException;
//import io.openenterprise.daisy.rs.model.DefaultApi;


DefaultApi apiInstance = new DefaultApi();
UUID id = new UUID(); // UUID | Entity ID
try {
    apiInstance.actionsChainIdHead(id);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#actionsChainIdHead");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | [**UUID**](.md)| Entity ID |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

<a name="actionsIdDelete"></a>
# **actionsIdDelete**
> actionsIdDelete(id)



### Example
```java
// Import classes:
//import io.openenterprise.daisy.rs.ApiException;
//import io.openenterprise.daisy.rs.model.DefaultApi;


DefaultApi apiInstance = new DefaultApi();
UUID id = new UUID(); // UUID | Entity ID
try {
    apiInstance.actionsIdDelete(id);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#actionsIdDelete");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | [**UUID**](.md)| Entity ID |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

<a name="actionsIdGet"></a>
# **actionsIdGet**
> Action actionsIdGet(id)



### Example
```java
// Import classes:
//import io.openenterprise.daisy.rs.ApiException;
//import io.openenterprise.daisy.rs.model.DefaultApi;


DefaultApi apiInstance = new DefaultApi();
UUID id = new UUID(); // UUID | Entity ID
try {
    Action result = apiInstance.actionsIdGet(id);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#actionsIdGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | [**UUID**](.md)| Entity ID |

### Return type

[**Action**](Action.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="actionsIdHead"></a>
# **actionsIdHead**
> actionsIdHead(id)



### Example
```java
// Import classes:
//import io.openenterprise.daisy.rs.ApiException;
//import io.openenterprise.daisy.rs.model.DefaultApi;


DefaultApi apiInstance = new DefaultApi();
UUID id = new UUID(); // UUID | Entity ID
try {
    apiInstance.actionsIdHead(id);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#actionsIdHead");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | [**UUID**](.md)| Entity ID |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

<a name="postAction"></a>
# **postAction**
> Action postAction(body)



### Example
```java
// Import classes:
//import io.openenterprise.daisy.rs.ApiException;
//import io.openenterprise.daisy.rs.model.DefaultApi;


DefaultApi apiInstance = new DefaultApi();
Action body = new Action(); // Action | 
try {
    Action result = apiInstance.postAction(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#postAction");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**Action**](Action.md)|  | [optional]

### Return type

[**Action**](Action.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="postActionsChain"></a>
# **postActionsChain**
> ActionsChain postActionsChain(body)



### Example
```java
// Import classes:
//import io.openenterprise.daisy.rs.ApiException;
//import io.openenterprise.daisy.rs.model.DefaultApi;


DefaultApi apiInstance = new DefaultApi();
ActionsChain body = new ActionsChain(); // ActionsChain | 
try {
    ActionsChain result = apiInstance.postActionsChain(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#postActionsChain");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**ActionsChain**](ActionsChain.md)|  | [optional]

### Return type

[**ActionsChain**](ActionsChain.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

