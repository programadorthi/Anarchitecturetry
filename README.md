# About
This project is a compilation of some best practices that I have found during my Android development career.

The behavior is to consume a public blockchain API and show the current value and a historical prices. 
The result is cached in the SQLite to be used when there is no internet connection.

[The project presentation](https://docs.google.com/presentation/d/1_Hc7TCL_04joiN7NWEqpxtd5qCQVgPFU3WIPwVsWoao/edit#slide=id.p)

# Project structure
Explore the project branches and modules to see how it works. The project architecture is MVVM only.

### Branches description
| Branch | Description |
| ------------- | ------------- |
| [master](https://github.com/programadorthi/Anarchitecturetry) | Monolithic using RxJava. Same the develop |
| [develop](https://github.com/programadorthi/Anarchitecturetry/tree/develop) | Monolithic using RxJava. |
| [develop-coroutines](https://github.com/programadorthi/Anarchitecturetry/tree/develop-coroutines) | Monolithic using Coroutines. |
| [multi-module](https://github.com/programadorthi/Anarchitecturetry/tree/multi-module) | Multi-module dynamic-feature using RxJava and Koin. |
| multi-module-coroutine (Soon) | Multi-module with dynamic-feature and using Coroutines. |

### Overview

The `app` module is the main entry point in all branches but in the `multi-module` the app module is a base feature to the `blockchain` module.

> `base` feature/module: It's a library with shared codes used in all other modules.

### Features organization

I have organized my code by feature. This avoid to share knowledge between feature without is required.
Below you see the package organization sample.

- **feature1**
  * `data` - Check internet connection, network request, API response mapper, database cache
  * `di` - Feature dependency injection configurations
  * `domain` - Business rules validations and data communications
  * `presentation` - UI state, UI formatted values
- **login** - A feature sample with other features and shared code between children features
  * `access` - An internal feature that is organized like feature1
  * `register` - An internal feature that is organized like feature1
  * `SharedCode.kt` - A code that is used in the login feature and its children features

# WARNING

If you are beginning in Android or your project is below SDK 23, you should use `minSdkVersion`. `minSdkVersionDev` greater than or equals to 23 is for Android experts only!!!

# Credits
  * [Blockchain](https://www.blockchain.com/) for the public Blockchain API
