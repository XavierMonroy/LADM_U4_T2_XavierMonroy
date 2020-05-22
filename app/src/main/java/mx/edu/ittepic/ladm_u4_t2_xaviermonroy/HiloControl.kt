package mx.edu.ittepic.ladm_u4_t2_xaviermonroy

class HiloControl (p: MainActivity) : Thread() {
    private var inicio = false
    private var puntero = p

    override fun run() {
        super.run()
        inicio = true
        while (inicio) {
            sleep(1000)
            puntero.runOnUiThread {
                puntero.obtenerLlamadas()
            }
        }
    }
}