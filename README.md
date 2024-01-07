
# OAuth2 - Sample Java Github App

## Table of Contents

* [Requirements](#requirements)
* [First Use Instructions](#first-use-instructions)
* [Running the code](#running-the-code)
* [Configuring the callback endpoint](#configuring-the-callback-endpoint)
* [Getting the OAuth Tokens](#getting-the-oauth-tokens)
* [Storing the Tokens](#storing-the-tokens)


## Requirements

In order to successfully run this sample app you need a few things:

1. Java 1.8
2. A [github.com](https://github.com) account
3. An oauth app on [github.com](https://github.com) and the associated client id and client secret.
 
## First Use Instructions

1. Clone the GitHub repo to your computer
2. Fill in the [`application.properties`](src/main/resources/application.properties) file values (OAuth2AppClientId, OAuth2AppClientSecret) by copying over from the keys section for your app.

## Running the code

Once the sample app code is on your computer, you can do the following steps to run the app:

1. cd to the project directory</li>
2. Run the command:`./gradlew bootRun` (Mac OS) or `gradlew.bat bootRun` (Windows)</li>
3. Wait until the terminal output displays the "Started Application in xxx seconds" message.
4. Your app should be up now in http://localhost:8080/ 
5. The oauth2 callback endpoint in the sample app is http://localhost:8080/oauth2redirect
6. To run the code on a different port, uncomment and update server.port property in application.properties

## Configuring the callback endpoint
You'll have to set a Redirect URI in the developer settings. With this app, the typical value would be http://localhost:8080/oauth2redirect, unless you host this sample app in a different way (if you were testing HTTPS, for example).

Note: Using localhost and http will only work when developing, using the sandbox credentials. Once you use production credentials, you'll need to host your app over https.

## Getting the OAuth Tokens

The sample app supports the following flows:

**Sign In With Github** - this flow requests OpenID only scopes.  Feel free to change the scopes being requested in `application.properties`.  After authorizing (or if the account you are using has already been authorized for this app), the redirect URL (`/oauth2redirect`) will parse the JWT ID token, and make an API call to the user information endpoint.

## Storing the tokens
This app stores all the tokens and user information in the session. For production ready app, tokens should be encrypted and stored in a database.

