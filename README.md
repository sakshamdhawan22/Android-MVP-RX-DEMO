# Android-MVP-RX-DEMO (Stock Tracker)
This is a sample Android app( a realtime stock tracker) to showcase MVP Android architecture with RxAndroid, Retrofit 2.0, ButterKnife. To try out the app right away, go here 
https://play.google.com/store/apps/details?id=com.application.saksham.stocktracker

I create this boilerplate for all the little apps I create. I'm kind of tired of doing it. Now it's opensource and meant to
serve as a reference to the must have tools when you start building a new android app.

##  What are all the tools used ?
1. RxJava
2. RxAndroid
3. Timber
4. Retrofit 2.0
5. Butterknife
6. Android platform data binding
7. GSON
8. Stetho
9. Firebase Job Dispatcher

##  What does this app do ?
This app is a basic stock tracker that shows you  the realtime current stock price, lowest intraday price, highest intraday price and the price history based on the data provided from https://www.alphavantage.co/. This app can break if the folks at https://www.alphavantage.co/ change the json schema. The prices are fully reactive and refresh in real time. Some examples of the stock symbols you can enter

  MSFT<br />
  GOOG <br />
  MCD<br />
  BAC<br />
  NSE:HDFC<br />
  
  Please refer to https://www.alphavantage.co/ for more information on how to prefix stock market codes.
  
  


<img src="https://i.imgur.com/T40LNSG.png" width="400">
<img src="https://i.imgur.com/4RG2Jpg.png" width="400">

<img src="https://i.imgur.com/E3VXC6W.png" width="400">
