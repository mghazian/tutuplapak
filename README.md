# Tutuplapak

Backend application for online marketplace.

Built using Java 21, PostgreSQL, and MinIO.

## Core Feature

- Inventory management for merchant
- Purchasing system for customer
- Multi-tenant support

## Additional Feature

- Profile management

## Getting Started

### Installation

Clone this repository
```sh
git clone https://github.com/mghazian/tutuplapak.git
```

### Setting up

#### Prerequisite

`podman` and `podman-compose` is required.

1. Install [podman](https://podman.io/).
2. For windows user, make sure the podman machine has been started.
3. Install podman-compose (ensure Python has been installed)
   ```shell
   pip install podman-compose
   podman-compose --version # Make sure the software installed successfully
   ```

### Running the backend

Open terminal on this repository's directory, then run
```shell
podman-compose up -d
```

This will launch the backend, database, and blob server. If the process run successfully, the backend can be accessed via `http://localhost:8080`.

## Usage Guide

The following is the available endpoints provided by the backend.

| Feature | Method | Endpoint | Authorization | User Role | Description |
| :--- | :--- | :--- | :--- | :--- | :--- |
| Registration (Email) | `POST` | `/v1/register/email` | No | Seller | Creates a new seller account using email. |
| Registration (Phone) | `POST` | `/v1/register/phone` | No | Seller | Creates a new seller account using a phone number. |
| Login (Email) | `POST` | `/v1/login/email` | No | Seller | Authenticates seller and issues a **Bearer Token**. |
| Login (Phone) | `POST` | `/v1/login/phone` | No | Seller | Authenticates seller and issues a **Bearer Token**. |
| View Profile | `GET` | `/v1/user` | Yes | Seller | Retrieves current profile details (email, phone, bank, files). |
| **File Upload** | `POST` | `/v1/file` | No | Seller/Customer | Uploads an image (max 100KiB) and returns a `fileId` and URIs. |
| Update Profile | `PUT` | `/v1/user` | Yes | Seller | Updates bank account details and links a profile `fileId`. |
| Link Phone | `POST` | `/v1/user/link/phone` | Yes | Seller | Links a phone number to the authenticated account. |
| Link Email | `POST` | `/v1/user/link/email` | Yes | Seller | Links an email address to the authenticated account. |
| Create Product | `POST` | `/v1/product` | Yes | Seller | Creates a new product listing for sale. |
| Update Product | `PUT` | `/v1/product/:productId` | Yes | Seller | Fully updates an existing product's details. |
| Delete Product | `DELETE` | `/v1/product/:productId` | Yes | Seller | Removes a product from the inventory. |
| **Browse Products** | `GET` | `/v1/product` | No | Customer | **Public** endpoint to search, filter, and view products. |
| Checkout / Purchase | `POST` | `/v1/purchase` | No | Customer | Initiates a purchase, locks product details, and provides payment info. |
| Upload Payment Proof | `POST` | `/v1/purchase/:purchaseId` | No | Customer | Finalizes a purchase by uploading payment proof and **reducing inventory**. |

---

### Seller Registration

Registration can be done in two ways: by email and by phone.

#### Seller Registration by Email

```
POST /v1/register/email
```

##### Request body format

The request requires a valid email and a password between 8 and 32 characters.

```json
{
  "email": "<string: email>",
  "password": "<string>" 
}
```

##### Response format

On successful registration, the system returns the seller's email, an empty phone string, and an authentication token.

```json
{
  "email": "<string: email>",
  "phone": "<string: empty>", 
  "token": "<string: token>"
}
```

##### Example

```
POST /v1/register/email
```

**Request**

```json
{
  "email": "seller_one@tutuplapak.com",
  "password": "securepassword123"
}
```

**Response (201 Created)**

```json
{
  "email": "seller_one@tutuplapak.com",
  "phone": "",
  "token": "token_for_seller_one_email_reg" 
}
```

-----

#### Seller Registration by Phone

```
POST /v1/register/phone
```

##### Request body format

The request requires a valid international phone number (with `+` prefix) and a password between 8 and 32 characters.

```json
{
  "phone": "<string: international phone>",
  "password": "<string>" 
}
```

##### Response format

On successful registration, the system returns the seller's phone number, an empty email string, and an authentication token.

```json
{
  "phone": "<string: phone>",
  "email": "<string: empty>", 
  "token": "<string: token>"
}
```

##### Example

```
POST /v1/register/phone
```

**Request**

```json
{
  "phone": "+628123456789",
  "password": "securepassword123"
}
```

**Response (201 Created)**

```json
{
  "phone": "+628123456789",
  "email": "",
  "token": "token_for_seller_one_phone_reg" 
}
```

-----

### Seller Login

Similar to registration process, login is done in two ways: by phone or by email

-----

#### Seller Login by Email

```
POST /v1/login/email
```

#### Request body format

The seller provides their registered email and password to receive an authentication token.

```json
{
  "email": "<string: email>",
  "password": "<string>"
}
```

#### Response format

On successful login, the system returns the seller's email, their phone number (if linked, otherwise empty string), and an authentication token. This **token** must be used in the `Authorization: Bearer <token>` header for all subsequent authenticated API calls.

```json
{
  "email": "<string: email>",
  "phone": "<string: phone or empty>",
  "token": "<string: token>"
}
```

#### Example

```
POST /v1/login/email
```

**Request**

```json
{
  "email": "seller_one@tutuplapak.com",
  "password": "securepassword123"
}
```

**Response (200 OK)**

```json
{
  "email": "seller_one@tutuplapak.com",
  "phone": "",
  "token": "token_for_seller_one_email_login"
}
```

-----

#### Seller Login by Phone

```
POST /v1/login/phone
```

##### Request body format

The seller provides their registered phone number and password to receive an authentication token.

```json
{
  "phone": "<string: international phone>",
  "password": "<string>"
}
```

##### Response format

On successful login, the system returns the seller's phone number, their email (if linked, otherwise empty string), and an authentication token.

```json
{
  "phone": "<string: phone>",
  "email": "<string: email or empty>",
  "token": "<string: token>"
}
```

##### Example

```
POST /v1/login/phone
```

**Request**

```json
{
  "phone": "+628123456789",
  "password": "securepassword123"
}
```

**Response (200 OK)**

```json
{
  "phone": "+628123456789",
  "email": "",
  "token": "token_for_seller_one_phone_login"
}
```

-----

### See Seller (My) Profile

```
GET /v1/user
```

#### Header requirement

The authentication token must be provided in the `Authorization` header.

| Key | Value |
| :--- | :--- |
| `Authorization` | `Bearer <token>` |

#### Request body format

No request body is required for this endpoint.

#### Response format

Returns the seller's current profile data, including linked contact details, file information, and bank account details. Fields are returned as empty strings when not yet set.

```json
{
  "email": "<string: email or empty>",
  "phone": "<string: phone or empty>",
  "fileId": "<string: file ID or empty>",
  "fileUri": "<string: file URI or empty>",
  "fileThumbnailUri": "<string: thumbnail URI or empty>",
  "bankAccountName": "<string: or empty>",
  "bankAccountHolder": "<string: or empty>",
  "bankAccountNumber": "<string: or empty>"
}
```

#### Example

```
GET /v1/user
```

**Header**

```
Authorization: Bearer <token>
```

**Response (200 OK)**

The following is example of the initial profile response after registering with email and before setting up bank details.

```json
{
  "email": "seller_one@tutuplapak.com",
  "phone": "",
  "fileId": "",
  "fileUri": "",
  "fileThumbnailUri": "",
  "bankAccountName": "",
  "bankAccountHolder": "",
  "bankAccountNumber": ""
}
```

-----

### File Upload

This endpoint is used to upload any file (like a profile image or product image) and obtain the unique **`fileId`** needed to link the file to a profile or a product. **It does not require authentication.**

```
POST /v1/file
```

#### Request body format

The request must be sent as **Multipart Form-Data**.

| Key | Value Type | Description |
| :--- | :--- | :--- |
| `file` | `file` | JPEG/JPG/PNG file, maximum 100KiB. |

#### Response format

Returns the unique ID, URI, and thumbnail URI for the uploaded file.

```json
{
  "fileId": "<string: id>",
  "fileUri": "<string: file_uri>",
  "fileThumbnailUri": "<string: thumbnail_uri>"
}
```

#### Example

```
POST /v1/file
```

**Request** (Multipart Form-Data)

```
file: [binary content of profile_pic.jpg]
```

**Response (200 OK)**

```json
{
  "fileId": "f-4001",
  "fileUri": "http://tutuplapak.com/img/file-4001.jpg",
  "fileThumbnailUri": "http://tutuplapak.com/img/file-4001_thumb.jpg"
}
```

-----

### Seller Profile Update (Bank & Image)

This step uses the `fileId` obtained in the `File Upload` endpoint to link a profile image, along with setting up mandatory bank account information.

```
PUT /v1/user
```

#### Header requirement

The authentication token must be provided in the `Authorization` header.

| Key | Value |
| :--- | :--- |
| `Authorization` | `Bearer <token>` |

#### Request body format

Requires the seller's bank account information. The `fileId` is optional and is used to set the seller's profile image.

```json
{
  "fileId": "<string: file ID or empty>", // optional, must be a valid fileId from /v1/file
  "bankAccountName": "<string>", // minLength: 4, maxLength: 32
  "bankAccountHolder": "<string>", // minLength: 4, maxLength: 32
  "bankAccountNumber": "<string>" // minLength: 4, maxLength: 32
}
```

#### Response format

Returns the updated seller profile object, including the URIs for the newly linked file.

```json
{
  "email": "<string: email or empty>",
  "phone": "<string: phone or empty>",
  "fileId": "<string: file ID or empty>",
  "fileUri": "<string: file URI or empty>",
  "fileThumbnailUri": "<string: thumbnail URI or empty>",
  "bankAccountName": "<string: or empty>",
  "bankAccountHolder": "<string: or empty>",
  "bankAccountNumber": "<string: or empty>"
}
```

#### Example

```
PUT /v1/user
```

**Header**

```
Authorization: Bearer <token>
```

**Request**

```json
{
  "fileId": "f-4001",
  "bankAccountName": "BCA",
  "bankAccountHolder": "Seller One",
  "bankAccountNumber": "1234567890"
}
```

**Response (200 OK)**

```json
{
  "email": "seller_one@tutuplapak.com",
  "phone": "", 
  "fileId": "f-4001",
  "fileUri": "http://tutuplapak.com/img/file-4001.jpg", 
  "fileThumbnailUri": "http://tutuplapak.com/img/file-4001_thumb.jpg",
  "bankAccountName": "BCA",
  "bankAccountHolder": "Seller One",
  "bankAccountNumber": "1234567890"
}
```

-----

### Link Phone to Seller Profile

This endpoint allows a seller who registered with email to link a new phone number to their existing authenticated profile.

```
POST /v1/user/link/phone
```

#### Header requirement

The authentication token must be provided in the `Authorization` header.

| Key | Value |
| :--- | :--- |
| `Authorization` | `Bearer <token>` |

#### Request body format

The required international phone number (with `+` prefix) to be linked.

```json
{
  "phone": "<string: international phone>"
}
```

#### Response format

```json
{
  "email": "<string: email>",
  "phone": "<string: phone>",
  "fileId": "<string: empty>",
  "fileUri": "<string: empty>",
  "fileThumbnailUri": "<string: empty>",
  "bankAccountName": "<string: empty>",
  "bankAccountHolder": "<string: empty>",
  "bankAccountNumber": "<string: empty>"
}
```

#### Example

A user registered with email and has an un-updated profile.

```
POST /v1/user/link/phone
```

**Header**

```
Authorization: Bearer <token>
```

**Request**

```json
{
  "phone": "+628123456789"
}
```

**Response (200 OK)**

```json
{
  "email": "seller_one@tutuplapak.com",
  "phone": "+628123456789",
  "fileId": "",
  "fileUri": "",
  "fileThumbnailUri": "",
  "bankAccountName": "",
  "bankAccountHolder": "",
  "bankAccountNumber": ""
}
```

-----

### Link Email to Seller Profile

This endpoint allows a seller who registered with phone to link a new email to their existing authenticated profile.

```
POST /v1/user/link/email
```

#### Header requirement

The authentication token must be provided in the `Authorization` header.

| Key | Value |
| :--- | :--- |
| `Authorization` | `Bearer <token>` |

#### Request body format

The required email address to be linked.

```json
{
  "email": "<string: email>"
}
```

#### Response format

```json
{
  "email": "<string: email>",
  "phone": "<string: phone>",
  "fileId": "<string: empty>",
  "fileUri": "<string: empty>",
  "fileThumbnailUri": "<string: empty>",
  "bankAccountName": "<string: empty>",
  "bankAccountHolder": "<string: empty>",
  "bankAccountNumber": "<string: empty>"
}
```

#### Example

A user registered with phone and has an un-updated profile.

```
POST /v1/user/link/email
```

**Header**

```
Authorization: Bearer <token>
```

**Request**

```json
{
  "email": "seller_one@tutuplapak.com"
}
```

**Response (200 OK)**

```json
{
  "email": "seller_one@tutuplapak.com",
  "phone": "+628123456789",
  "fileId": "",
  "fileUri": "",
  "fileThumbnailUri": "",
  "bankAccountName": "",
  "bankAccountHolder": "",
  "bankAccountNumber": ""
}
```

-----

### Seller's Inventory Management

This section explains how seller can manage their inventory. The inventory data is inherently tied with a `category` which falls in one of the following value:

| **Types** |
| :-------- |
| Food      |
| Beverage  |
| Clothes   |
| Furniture |
| Tools     |


#### Create Product

This endpoint creates a new product listing. It requires the `fileId` obtained in `File Upload` endpoint to link an image to the product.

```
POST /v1/product
```

#### Header requirement

The authentication token must be provided in the `Authorization` header.

| Key | Value |
| :--- | :--- |
| `Authorization` | `Bearer <token>` |

#### Request body format

All fields are required to create a product. Category must match one of the defined enums.

```json
{
  "name": "<string>", // minLength: 4, maxLength: 32
  "category": "<string: enum>", 
  "qty": "<number>", // min: 1
  "price": "<number>", // min: 100
  "sku": "<string>", // maxLength: 32 (unique per account)
  "fileId": "<string: file ID>" // must be a valid fileId from /v1/file
}
```

#### Response format

Returns the full details of the newly created product, including linked file URIs and timestamps.

```json
{
  "productId": "<string: id>",
  "name": "<string>",
  "category": "<string>",
  "qty": "<number>",
  "price": "<number>",
  "sku": "<string>",
  "fileId": "<string>",
  "fileUri": "<string: file URI>",
  "fileThumbnailUri": "<string: thumbnail URI>",
  "createdAt": "<string: ISO Date>",
  "updatedAt": "<string: ISO Date>"
}
```

#### Example (Create)

```
POST /v1/product
```

**Header**

```
Authorization: Bearer <token>
```

**Request**

```json
{
  "name": "Organic Honey Jar",
  "category": "Food",
  "qty": 50,
  "price": 150000,
  "sku": "HONEY-ORG-001",
  "fileId": "f-4002"
}
```

**Response (201 Created)**

```json
{
  "productId": "p-201",
  "name": "Organic Honey Jar",
  "category": "Food",
  "qty": 50,
  "price": 150000,
  "sku": "HONEY-ORG-001",
  "fileId": "f-4002",
  "fileUri": "http://tutuplapak.com/img/file-4002.jpg",
  "fileThumbnailUri": "http://tutuplapak.com/img/file-4002_thumb.jpg",
  "createdAt": "2025-10-17T11:00:00Z",
  "updatedAt": "2025-10-17T11:00:00Z"
}
```

-----

### Update Product

This endpoint fully replaces the existing product details. All fields are required in the request body.

```
PUT /v1/product/:productId
```

#### Header requirement

The authentication token must be provided in the `Authorization` header.

| Key | Value |
| :--- | :--- |
| `Authorization` | `Bearer <token>` |

#### Request body format

All fields are required, even if they remain unchanged.

```json
{
  "name": "<string>", 
  "category": "<string: enum>", 
  "qty": "<number>", 
  "price": "<number>", 
  "sku": "<string>", 
  "fileId": "<string: file ID>" 
}
```

#### Response format

Returns the updated product details with a new `updatedAt` timestamp.

```json
{
  "productId": "<string: id>",
  "name": "<string>",
  "category": "<string>",
  "qty": "<number>",
  "price": "<number>",
  "sku": "<string>",
  "fileId": "<string>",
  "fileUri": "<string: file URI>",
  "fileThumbnailUri": "<string: thumbnail URI>",
  "createdAt": "<string: ISO Date>",
  "updatedAt": "<string: ISO Date>"
}
```

#### Example (Update)

*Changing the price of product `p-201`.*

```
PUT /v1/product/p-201
```

**Header**

```
Authorization: Bearer <token>
```

**Request**

```json
{
  "name": "Organic Honey Jar",
  "category": "Food",
  "qty": 50,
  "price": 140000, // Updated price
  "sku": "HONEY-ORG-001",
  "fileId": "f-4002"
}
```

**Response (200 OK)**

```json
{
  "productId": "p-201",
  "name": "Organic Honey Jar",
  "category": "Food",
  "qty": 50,
  "price": 140000,
  "sku": "HONEY-ORG-001",
  "fileId": "f-4002",
  "fileUri": "http://tutuplapak.com/img/file-4002.jpg",
  "fileThumbnailUri": "http://tutuplapak.com/img/file-4002_thumb.jpg",
  "createdAt": "2025-10-17T11:00:00Z",
  "updatedAt": "2025-10-17T12:00:00Z"
}
```

-----

### Delete Product

This endpoint deletes a product from the seller's inventory.

```
DELETE /v1/product/:productId
```

#### Header requirement

The authentication token must be provided in the `Authorization` header.

| Key | Value |
| :--- | :--- |
| `Authorization` | `Bearer <token>` |

#### Request body format

No request body is required for this endpoint.

#### Response format

Returns a `200 OK` status code upon successful deletion. No content is expected in the response body.

```
(No Content)
```

#### Example (Delete)

```
DELETE /v1/product/p-201
```

**Header**

```
Authorization: Bearer <token>
```

**Response (200 OK)**

```
(Success, product deleted)
```

-----

### Browse Available Products

This is a public endpoint. This endpoint allows anyone (including unauthenticated customers) to view available product listings. It supports extensive filtering and sorting via query parameters.

```
GET /v1/product
```

#### Header requirement

**No authorization header is required** for this public endpoint.

#### Request parameter format

All parameters are optional and filter the result set using `AND` logic. Default pagination is `limit=5` and `offset=0`.

| Parameter | Type | Description | Example |
| :--- | :--- | :--- | :--- |
| `limit` | `number` | Max number of records to return. Default: 5. | `limit=10` |
| `offset` | `number` | Number of records to skip (for pagination). Default: 0. | `offset=20` |
| `productId` | `string` | Filter for an exact match of a product ID. | `productId=p-201` |
| `sku` | `string` | Filter for an exact match of a SKU. | `sku=HONEY-ORG-001` |
| `category` | `string` | Filter by category enum (`Food`, `Beverage`, etc.). | `category=Food` |
| `sortBy` | `string: enum` | Sorting criteria. Enums: `newest`, `oldest`, `cheapest`, `expensive`. | `sortBy=cheapest` |

#### Response format

Returns an array of product objects matching the applied filters and sorting.

```json
[
  {
    "productId": "<string: id>",
    "name": "<string>",
    "category": "<string>",
    "qty": "<number>",
    "price": "<number>",
    "sku": "<string>",
    "fileId": "<string>",
    "fileUri": "<string: file URI>",
    "fileThumbnailUri": "<string: thumbnail URI>",
    "createdAt": "<string: ISO Date>",
    "updatedAt": "<string: ISO Date>"
  }
]
```

#### Example

Retrieve the 10 cheapest "Food" products.

```
GET /v1/product?limit=10&category=Food&sortBy=cheapest
```

**Response (200 OK)**

```json
[
  {
    "productId": "p-201",
    "name": "Organic Honey Jar",
    "category": "Food",
    "qty": 50,
    "price": 140000,
    "sku": "HONEY-ORG-001",
    "fileId": "f-4002",
    "fileUri": "http://tutuplapak.com/img/file-4002.jpg",
    "fileThumbnailUri": "http://tutuplapak.com/img/file-4002_thumb.jpg",
    "createdAt": "2025-10-17T11:00:00Z",
    "updatedAt": "2025-10-17T12:00:00Z"
  },
  {
    "productId": "p-202",
    "name": "Spicy Chili Sauce",
    "category": "Food",
    "qty": 12,
    "price": 45000,
    "sku": "SAUCE-CHI-005",
    "fileId": "f-4003",
    "fileUri": "http://tutuplapak.com/img/file-4003.jpg",
    "fileThumbnailUri": "http://tutuplapak.com/img/file-4003_thumb.jpg",
    "createdAt": "2025-10-17T10:30:00Z",
    "updatedAt": "2025-10-17T10:30:00Z"
  }
]
```

-----

The seller has set up and listed products (Steps 1-7), and the customer has viewed them (Step 8). The next phase is the **Customer Checkout** process.

Here is **Step 9: Customer Checkout Products** (Unauthenticated).

-----

### Product Checkout

This is a public endpoint. This endpoint allows a customer to purchase items by specifying the products and their contact details. The system copies the product information at the time of purchase to prevent issues if the seller updates the product later.

```
POST /v1/purchase
```

#### Header requirement

**No authorization header is required** for this customer endpoint.

#### Request body format

The request includes an array of items being purchased and the customer's contact information. Note: the minimum quantity per item in the array is $\mathbf{2}$.

```json
{
  "purchasedItems": [
    {
      "productId": "<string: valid product ID>",
      "qty": "<number: min 2>"
    }
  ],
  "senderName": "<string>", // minLength: 4, maxLength: 55
  "senderContactType": "<string: enum>", // "email" or "phone"
  "senderContactDetail": "<string>" // validated based on senderContactType
}
```

#### Response format

Returns the details of the created purchase, including the copied product information, the calculated `totalPrice`, and the bank account details required for payment, grouped by seller.

```json
{
  "purchaseId": "<string: id>",
  "purchasedItems": [
    {
      "productId": "<string: id>",
      "name": "<string>",
      "category": "<string>",
      "qty": "<number>", // qty BEFORE bought
      "price": "<number>",
      "sku": "<string>",
      "fileId": "<string>",
      "fileUri": "<string: file URI>",
      "fileThumbnailUri": "<string: thumbnail URI>",
      "createdAt": "<string: ISO Date>",
      "updatedAt": "<string: ISO Date>"
    }
  ],
  "totalPrice": "<number>", // total price of all items
  "paymentDetails": [
    {
      "bankAccountName": "<string>",
      "bankAccountHolder": "<string>",
      "bankAccountNumber": "<string>",
      "totalPrice": "<number>" // total price for this specific seller
    }
  ]
}
```

#### Example

```
POST /v1/purchase
```

**Request**

```json
{
  "purchasedItems": [
    {
      "productId": "p-201", 
      "qty": 5
    }
  ],
  "senderName": "Customer Buyer",
  "senderContactType": "email",
  "senderContactDetail": "customer@example.com"
}
```

**Response (201 Created)**

```json
{
  "purchaseId": "buy-301",
  "purchasedItems": [
    {
      "productId": "p-201",
      "name": "Organic Honey Jar",
      "category": "Food",
      "qty": 50, // Qty before purchase
      "price": 140000,
      "sku": "HONEY-ORG-001",
      "fileId": "f-4002",
      "fileUri": "http://tutuplapak.com/img/file-4002.jpg",
      "fileThumbnailUri": "http://tutuplapak.com/img/file-4002_thumb.jpg",
      "createdAt": "2025-10-17T11:00:00Z",
      "updatedAt": "2025-10-17T12:00:00Z"
    }
  ],
  "totalPrice": 700000,
  "paymentDetails": [
    {
      "bankAccountName": "BCA",
      "bankAccountHolder": "Seller One",
      "bankAccountNumber": "1234567890",
      "totalPrice": 700000
    }
  ]
}
```

-----

### Payment Proof Upload

This is a public endpoint. This endpoint is used after the customer has made an external bank transfer (using the details from `Product Checkout` endpoint). The customer uploads payment proof images (which must correspond to the number of unique sellers in the purchase) using their respective `fileId`s.

```
POST /v1/purchase/:purchaseId
```

#### Header requirement

**No authorization header is required** for this customer endpoint.

#### Path parameter

The ID of the purchase created in endpoint `Product Checkout`.

| Parameter | Type | Description |
| :--- | :--- | :--- |
| `purchaseId` | `string` | The ID of the transaction being paid for (e.g., `buy-301`). |

#### Request body format

An array containing the **`fileId`** for each payment proof. The number of file IDs must match the number of unique sellers/payment details in the original purchase.

```json
{
  "fileIds": [ // array | required | number of fileIds must match number of unique sellers
    "<string: file ID>" 
  ]
}
```

#### Response format

Returns a `201 Created` status code upon successful processing of the payment proof. No content is expected in the response body.

```
(No Content)
```

#### Example

```
POST /v1/purchase/buy-301
```

**Request**

```json
{
  "fileIds": [
    "f-5001" 
  ]
}
```

**Response (201 Created)**

```
(Success, payment proof recorded and inventory reduced)
```
