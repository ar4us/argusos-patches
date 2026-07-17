package app.argusos.patches.twitter.misc.hook

@Suppress("unused")
val hideRecommendedUsersPatch = hookPatch(
    name = "Hide recommended users",
    hookClassDescriptor = "Lapp/argusos/extension/twitter/patches/hook/patch/recommendation/RecommendedUsersHook;",
)
