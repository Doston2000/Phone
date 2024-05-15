package uz.lazydevelopers1.callPhone

interface FragmentListener {
    fun openPermissionActivity()
    fun listenKeypadInput(phoneNumber: String)
    fun backPress()
}