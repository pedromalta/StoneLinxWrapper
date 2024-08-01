package dev.pedromalta.app

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.linx.mobile.logger.Log
import dev.pedromalta.stonelinxwrapper.modules.auth.Authentication
import dev.pedromalta.stonelinxwrapper.modules.auth.model.CredentialInfo
import dev.pedromalta.stonelinxwrapper.modules.credit.Credit
import dev.pedromalta.stonelinxwrapper.modules.info.Info
import dev.pedromalta.stonelinxwrapper.view.NoOpUiCallback

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MAIN ACTIVITY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.button_auth).setOnClickListener {
            val auth = Authentication(
                this,
                NoOpUiCallback,
                CredentialInfo(
                    "62774176000158",
                    "Rw\$b05;\$m2J6}Gq7wh@]"
                )
            )
            auth.authenticate(
                onFailure = {
                    Log.d(TAG, "Falha na autenticação")
                    resultDialog("Falha na autenticação")
                },
                onSuccess = { nsu: String ->
                    Log.d(TAG, "Autenticação ok! NSU: $nsu")
                    resultDialog("Autenticação ok! NSU: $nsu")
                }
            )
        }

        findViewById<Button>(R.id.button_info).setOnClickListener {
            val getInfo = Info().getInfo(this)
            val message = "version: ${getInfo.sdkExternalVersion}\n" + //versao
                    "sdkVersion: ${getInfo.sdkVersion}\n" + //versao do sdk
                    "buildType: ${getInfo.buildType}\n" + //informa se build release ou debug
                    "profile: ${getInfo.profile}\n" + //indica o profile do build
                    "hardware: ${getInfo.hardwareManufacturer}\n" + //fabricante do terminal
                    "modelo: ${getInfo.hardwareModel}\n" + //modelo do terminal
                    "impressora: ${getInfo.isPrintSupported}\n" + //suporte a impressora
                    "smartPOS: ${getInfo.isSmartPOS}" //indica se smartPOS
            val dialogBuilder = AlertDialog.Builder(this)
            dialogBuilder.setTitle("GetInfo")
            dialogBuilder.setMessage(message)
            dialogBuilder.setCancelable(false)
            dialogBuilder.setNegativeButton("Ok") { dialog, _ ->
                dialog.dismiss()
            }
            dialogBuilder.create().show()
        }

        findViewById<Button>(R.id.button_credit).setOnClickListener {
            val credit = Credit(
                this,
                NoOpUiCallback
            )
            credit.transact(
                mapOf(
                    "installment" to "1",
                    "amount" to "100.0",
                    "cardNumber" to "1234567890123456",
                    "financialType" to "ONE_INSTALMENT"
                ),
                autoConfirm = true,
                onFailure = {
                    Log.d(TAG, "Falha na transação")
                    resultDialog("Falha na transação")
                },
                onSuccess = {
                    Log.d(TAG, "Transação ok!")
                    resultDialog("Transação ok!")
                }
            )
        }
    }

    private fun resultDialog(msg: String) {
        runOnUiThread {
            val builder = AlertDialog.Builder(this)
            builder.setTitle(msg)
            builder.setCancelable(false)
            builder.setNegativeButton("Ok") { dialog, _ ->
                dialog.dismiss()
            }
            builder.create().show()
        }
    }
}