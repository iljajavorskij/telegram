package com.example.mymassenger.utilits

enum class AppState(val state:String) {
    ONLINE("в сети"),
    OFFLINE("быд недавно"),
    TYPING("печатает");


    companion object{
        fun updateState(appState: AppState){
            REF_DATABASE_ROOT.child(NODE_USER).child(UID).child(CHILD_STATE)
                .setValue(appState.state)
                .addOnSuccessListener {
                    USER.state = appState.state
                }
                .addOnFailureListener{
                    showToast("fail")
                }
        }
    }
}