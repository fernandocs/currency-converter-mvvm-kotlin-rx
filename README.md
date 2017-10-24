# MVVP

<img src="https://raw.githubusercontent.com/fernandocs/currency-converter-mvvm-kotlin-rx/master/images/mvvm-flow.png" alt="layers" height="80%" width="80%">

##### The View
- Activity / Fragment / View
- Requests relevant UI data from the ViewModel
- Requests the ViewModel to perform operations on the Data (insert/edit data based on user input )

##### The ViewModel
- Acts as a bridge between the View and the Model
Requests/aggregates data from the Model, and transforms it for the View
- Expose means for the View to update the Model

##### The Model
- So know as DataModel / Repository
Holds the business logic
- Serves data from various sources (DB, REST Api, cache)


<img src="https://raw.githubusercontent.com/fernandocs/currency-converter-mvvm-kotlin-rx/master/images/core.png" alt="layers"/>

<img src="https://raw.githubusercontent.com/fernandocs/currency-converter-mvvm-kotlin-rx/master/images/app.png" alt="layers"/>

# Libraries

[RxJava](https://github.com/ReactiveX/RxJava)

[Retrofit](https://github.com/square/retrofit)

[Mockito Kotlin](https://github.com/nhaarman/mockito-kotlin)
