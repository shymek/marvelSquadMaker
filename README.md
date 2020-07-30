Android Tech Test Project
=========================

This repo contains a project I was required to implement for a tech test.

While doing it I tried to use some new things I never used before which are

- Coroutines
- Koin
- LiveData

Hence parts with these bits might seem odd.

**General Architecture**

In this project I used MVVM with [DataBinding](https://developer.android.com/topic/libraries/data-binding) and UseCases along with [ViewModels](https://developer.android.com/topic/libraries/architecture/viewmodel) from Google.

For navigation I used [Jetpack Navigation](https://developer.android.com/guide/navigation)

For storage I went with [Room](https://developer.android.com/training/data-storage/room)

And for HTTP requests I used [Retrofit](https://square.github.io/retrofit/)

And lastly for loading images I used [Glide](https://github.com/bumptech/glide)

**Testing**

For testing I used Junit4 and implemented only UnitTests for ViewModels and UseCases.
I had some trouble with testing `suspend` functions hence there's a Suppress annotation on some of them.

**My Takeaways**

Koin turned out to be easy to learn but I imagine it might get messy in larger projects.
Especially the fact that every class has to be `provided` explicitly unlike in Dagger2 when every dependency was provided elsewhere we could just use the @Inject annotation and that was it.
