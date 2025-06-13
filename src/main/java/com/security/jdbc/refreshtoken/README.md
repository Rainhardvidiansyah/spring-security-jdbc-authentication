

## Refresh Token Module

This module is specifically designed to work with the `refresh_token` table. This table also has a relationship with the `user`, represented by the `user_id` (referring to the `id` in the `users` table).

### Table Structure

| Column      | Data Type | Description                                     |
|-------------| --------- |-------------------------------------------------|
| id          | UUID      | Primary key                                     |
| user\_id    | UUID      | Foreign key referencing `users.id`              |
| token       | TEXT      | The refresh token itself                        |
| revoked     | BOOLEAN   | Token status, `true` if no longer valid/revoked |
| created\_at | TIMESTAMP | Time when the token was created                 |
| expires\_at | TIMESTAMP | Time when the token will be expired             |
| updated\_at | TIMESTAMP | Last time the token was updated                 |

---

### Core Functions in the `refreshtoken` Module

Below is an explanation of the main functions available in the module:

#### Save Refresh Token

This function is used to store a new refresh token in the database, along with the `user_id` of the token owner. The token is stored in an active state (`revoked == false`).

**Typical usage flow:**

* After a successful login, when both access token and refresh token are generated.
* The refresh token is saved for future re-authentication needs.

#### Revoke a Token

This function marks a refresh token as invalid (revoked). It is typically used when:

* A user logs out
* The system detects suspicious activity
* Token rotation process is triggered

Revoked tokens can no longer be reused.

#### Track User by Refresh Token

This function is used to find out who owns a particular refresh token. It's useful for validating the token and safely regenerating a new access token.

---

### Security Notes

* Refresh tokens should be securely stored on the client side (typically in an `httpOnly` cookie).
* Always check if a token has been revoked before reusing it.
* Old tokens should be revoked immediately after a new one is issued (token rotation).
* Consider setting an expiry time for refresh tokens to limit session duration.

---

