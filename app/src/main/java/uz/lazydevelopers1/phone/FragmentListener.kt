package uz.lazydevelopers1.phone

interface FragmentListener {
    fun openPermissionActivity()
    fun listenKeypadInput(phoneNumber: String)
    fun backPress()
}