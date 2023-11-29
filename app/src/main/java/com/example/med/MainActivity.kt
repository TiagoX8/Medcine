package com.example.med

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.OnInitListener
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.HorizontalScrollView
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity(), OnInitListener {
    private lateinit var textToSpeech: TextToSpeech

    private var usuarioTemp = Usuario(null, null, null, null)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)


        textToSpeech = TextToSpeech(this, this)
        setupButtons() //começa a chamar as funções


    }

    override fun onDestroy() {
        super.onDestroy()
        // Certifique-se de liberar os recursos do TextToSpeech ao destruir a atividade
        textToSpeech.stop()
        textToSpeech.shutdown()
    }


    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            // Configura a língua, se necessário
            val result = textToSpeech.setLanguage(Locale.getDefault())
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(this, "Linguagem não suportada.", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Falha na inicialização do TextToSpeech.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun speak(text: String) {
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }


    private fun setupButtons() {
        val btnNextToFormNome = findViewById<ImageButton>(R.id.bntpg) //coloca o botão aqui

        btnNextToFormNome.setOnClickListener {
            setContentView(R.layout.activity_main1) //chamando a página que vai aparecer
            setupFormnome() //chama a função pro form2 ai tem que colocar o resto das coisas

            // Adicione a fala aqui


        }
    }


    private var nomePaciente: String = ""

    private fun setupFormnome() {


        val imagem1 = findViewById<ImageView>(R.id.imageView10)
        val imagem2 = findViewById<ImageView>(R.id.imageView11)
        val btnNextToForm = findViewById<ImageButton>(R.id.bntpg1) //coloca o botão aqui
        val btnNextToReturn = findViewById<ImageButton>(R.id.return1) //coloca o botão aqui
        speak("Eu sou Daiki! Assistente Virtual da Clínica CDL. Qual seu nome")

        btnNextToReturn.setOnClickListener {
            setContentView(R.layout.activity_main) //chamando a página que vai aparecer
            setupButtons() //chama a função pro form3 ai tem que colocar o resto das coisas


        }



        btnNextToForm.setOnClickListener {
            val txtNome = findViewById<EditText>(R.id.txtNome)
            val nome = txtNome.text.toString()

            if (nome.isBlank()) {
                // Nome não foi digitado, não avança para a próxima página
                // Exibe um aviso se desejar
                Toast.makeText(
                    this,
                    "Por favor, digite seu nome antes de prosseguir.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                // Nome foi digitado, avance para a próxima página
                nomePaciente = nome // Armazena o nome na variável
                setContentView(R.layout.activity_main2) // Chamando a próxima página
                setupFormEscolha() // Configurar a próxima página

                // Adicione a fala aqui

            }
        }

        val txtNome = findViewById<EditText>(R.id.txtNome)

        // Adicione um TextWatcher ao EditText do nome
        txtNome.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Não é necessário implementar isso
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Não é necessário implementar isso
            }

            override fun afterTextChanged(s: Editable?) {
                // Verifique se o texto não está vazio
                s?.let {
                    if (it.isNotBlank()) {
                        // Capitalize a primeira letra de cada palavra e defina o texto de volta no EditText
                        val capitalizedText = it.toString().capitalizeFirstLetterOfWords()
                        if (capitalizedText != it.toString()) {
                            txtNome.setText(capitalizedText)
                            // Mova o cursor para o final do texto
                            txtNome.setSelection(capitalizedText.length)
                        }
                    }
                }
            }
        })

// Adicione um TextWatcher ao EditText do nome
        txtNome.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Não é necessário implementar isso
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Não é necessário implementar isso
            }

            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    // Remova todos os caracteres que não são letras
                    val filteredText = it.toString().replace(Regex("[^a-zA-Z ]"), "")
                    if (filteredText != it.toString()) {
                        txtNome.setText(filteredText)
                        // Mova o cursor para o final do texto
                        txtNome.setSelection(filteredText.length)
                    }
                }
            }
        })


        // Animação para imagem1
        imagem1.alpha = 0f
        imagem1.animate()
            .alpha(1f)
            .setDuration(1000)
            .withStartAction {
                imagem1.visibility = ImageView.VISIBLE
            }
            .start()

        // Animação para imagem2 (inicia depois de 1 segundo)
        imagem2.alpha = 0f
        imagem2.postDelayed({
            imagem2.animate()
                .alpha(1f)
                .setDuration(1000)
                .withStartAction {
                    imagem2.visibility = ImageView.VISIBLE
                }
                .start()
        }, 1000)


    }


    fun String.capitalizeFirstLetterOfWords(): String {
        val words = this.split(" ").map { it.trim() }
        val capitalizedWords = words.map { it.capitalize() }
        return capitalizedWords.joinToString(" ")
    }


    private fun setupFormEscolha() {

        val imagem3 = findViewById<ImageView>(R.id.imageView3)
        val imagem4 = findViewById<ImageView>(R.id.imageView5)
        val btnNextToRegister = findViewById<ImageButton>(R.id.register) //coloca o botão aqui
        val btnNextToLogin = findViewById<ImageButton>(R.id.login) //coloca o botão aqui
        val btnNextToReturn = findViewById<ImageButton>(R.id.return2) //coloca o botão aqui
        val txtNomePaciente = findViewById<TextView>(R.id.nomePaci)
        txtNomePaciente.text = nomePaciente
        speak("Que Lega te ver por aqui, $nomePaciente... Escolha o que vamos fazer")


        btnNextToReturn.setOnClickListener {
            setContentView(R.layout.activity_main1) //chamando a página que vai aparecer
            setupFormnome() //chama a função pro form3 ai tem que colocar o resto das coisas
        }


        btnNextToRegister.setOnClickListener {
            setContentView(R.layout.activity_main3) //chamando a página que vai aparecer


            setupFormCadCPF() //chama a função pro form3 ai tem que colocar o resto das coisas
        }



        btnNextToLogin.setOnClickListener {
            setContentView(R.layout.activity_main5) //chamando a página que vai aparecer


            setupFormLogin() //chama a função pro form3 ai tem que colocar o resto das coisas
        }



        // Animação para imagem3
        imagem3.alpha = 0f
        imagem3.animate()
            .alpha(1f)
            .setDuration(1000)
            .withStartAction {
                imagem3.visibility = ImageView.VISIBLE
            }
            .start()

        // Animação para imagem4 (inicia depois de 1 segundo)
        imagem4.alpha = 0f
        imagem4.postDelayed({
            imagem4.animate()
                .alpha(1f)
                .setDuration(1000)
                .withStartAction {
                    imagem4.visibility = ImageView.VISIBLE
                }
                .start()
        }, 1000)


    }



    private fun setupFormCadCPF() {

        val imagem5 = findViewById<ImageView>(R.id.imageView7)
        val imagem6 = findViewById<ImageView>(R.id.imageView6)
        val btnNext = findViewById<ImageButton>(R.id.bntpg3) //coloca o botão aqui
        val btnNextToReturn = findViewById<ImageButton>(R.id.return3) //coloca o botão aqui
        speak("Tudo bem! Vou te ajudar. Digite seu CPF")

        btnNextToReturn.setOnClickListener {
            setContentView(R.layout.activity_main2) // Vá para a próxima página
            setupFormEscolha() // Chama a função para o próximo layout
        }

        btnNext.setOnClickListener {
            val cpfEditText = findViewById<EditText>(R.id.EditCPF)
            val cpf = cpfEditText.text.toString()


            if (isValidCPF(cpf)) {
                // CPF válido, armazene temporariamente no objeto Usuario e avance para a próxima página
                usuarioTemp.cpf = cpf

                // Salva os dados do usuário no Firebas



                // Avance para a próxima etapa (coleta de email)
                setContentView(R.layout.activity_main4) //chamando a página que vai aparecer



                setupFormCadEmail() //chama a função pro form3 ai tem que colocar o resto das coisas



            } else {
                // CPF inválido, exiba uma mensagem de erro
                Toast.makeText(this, "CPF inválido", Toast.LENGTH_SHORT).show()
            }


            // Animação para imagem5
            imagem5.alpha = 0f
            imagem5.animate()
                .alpha(1f)
                .setDuration(1000)
                .withStartAction {
                    imagem5.visibility = ImageView.VISIBLE
                }
                .start()

            // Animação para imagem6 (inicia depois de 1 segundo)
            imagem6.alpha = 0f
            imagem6.postDelayed({
                imagem6.animate()
                    .alpha(1f)
                    .setDuration(1000)
                    .withStartAction {
                        imagem6.visibility = ImageView.VISIBLE
                    }
                    .start()
            }, 1000)


        }
    }

    private fun isValidCPF(cpf: String): Boolean {
        // Limpe o CPF removendo espaços em branco e caracteres especiais
        val cleanedCPF = cpf.replace("[^0-9]".toRegex(), "")

        // Verifique se o CPF tem 11 dígitos
        if (cleanedCPF.length != 11) {
            return false
        }

        // Verifique se todos os dígitos são iguais (exemplo: 111.111.111-11)
        if (cleanedCPF.matches("(\\d)\\1*".toRegex())) {
            return false
        }

        // Calcule o primeiro dígito verificador
        var sum = 0
        for (i in 0 until 9) {
            val digit = cleanedCPF[i].toString().toInt()
            sum += digit * (10 - i)
        }
        var remainder = sum % 11
        val firstVerifier = if (remainder < 2) 0 else 11 - remainder

        // Calcule o segundo dígito verificador
        sum = 0
        for (i in 0 until 10) {
            val digit = cleanedCPF[i].toString().toInt()
            sum += digit * (11 - i)
        }
        remainder = sum % 11
        val secondVerifier = if (remainder < 2) 0 else 11 - remainder

        // Verifique se os dígitos verificadores são iguais aos dígitos fornecidos no CPF
        return cleanedCPF[9].toString().toInt() == firstVerifier && cleanedCPF[10].toString().toInt() == secondVerifier
    }

    private var emailTemp: String = ""
    private fun setupFormLogin(){

        val imagem9 = findViewById<ImageView>(R.id.imageView16)
        val imagem10 = findViewById<ImageView>(R.id.imageView19)
        val btnNext = findViewById<ImageButton>(R.id.bntpg5) //coloca o botão aqui
        val btnNextToReturn = findViewById<ImageButton>(R.id.return5) //coloca o botão aqui
        speak("Certo!Vamos fazer o login. Agora digita seu email.")

        btnNextToReturn.setOnClickListener {
            setContentView(R.layout.activity_main2) //chamando a página que vai aparecer
            setupFormEscolha() //chama a função pro form3 ai tem que colocar o resto das coisas
        }



        btnNext.setOnClickListener {
            val emailEditText = findViewById<EditText>(R.id.txtEmailLog)
            val email = emailEditText.text.toString()

            // Armazena temporariamente o email
            emailTemp = email

            // Consulta o Firestore para verificar se o e-mail está cadastrado
            val db = FirebaseFirestore.getInstance()
            val usuariosRef = db.collection("Users")

            usuariosRef.whereEqualTo("email", email)
                .get()
                .addOnSuccessListener { documents ->
                    if (!documents.isEmpty) {
                        // O e-mail está cadastrado, avance para a próxima etapa (senha)
                        setContentView(R.layout.activity_main6)
                        setupFormSenhalog()
                        speak("Digite agora sua senha.")

                    } else {
                        // E-mail não cadastrado, exiba uma mensagem de erro
                        Toast.makeText(this, "E-mail não cadastrado", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->
                    // Trate os erros de consulta, se necessário
                    Toast.makeText(this, "Erro ao verificar o e-mail", Toast.LENGTH_SHORT).show()
                }
        }

        // Animação para imagem9
        imagem9.alpha = 0f
        imagem9.animate()
            .alpha(1f)
            .setDuration(1000)
            .withStartAction {
                imagem9.visibility = ImageView.VISIBLE
            }
            .start()

        // Animação para imagem10 (inicia depois de 1 segundo)
        imagem10.alpha = 0f
        imagem10.postDelayed({
            imagem10.animate()
                .alpha(1f)
                .setDuration(1000)
                .withStartAction {
                    imagem10.visibility = ImageView.VISIBLE
                }
                .start()
        }, 1000)




    }

    private fun setupFormCadEmail() {


        val imagem7 = findViewById<ImageView>(R.id.imageView14)
        val imagem8 = findViewById<ImageView>(R.id.imageView15)
        val btnNext = findViewById<ImageButton>(R.id.bntpg4) //coloca o botão aqui
        val btnNextToReturn = findViewById<ImageButton>(R.id.return4) //coloca o botão aqui
        speak("Vamos começar com seu e-mail. Digite.")

        btnNextToReturn.setOnClickListener {
            setContentView(R.layout.activity_main3) // chamando a página que vai aparecer
            setupFormCadCPF() // chama a função pro form3 ai tem que colocar o resto das coisas


        }


        btnNext.setOnClickListener {
            val emailEditText = findViewById<EditText>(R.id.txtEmail)
            val email = emailEditText.text.toString()

            if (isValidEmail(email)) {
                usuarioTemp.email = email


                // Salva os dados do usuário no Firebase



                // Avance para a próxima etapa (coleta de telefone)
                setContentView(R.layout.activity_main8)


                setupFormCadFone()




            } else {
                // Exiba uma mensagem de erro se o email for inválido
                Toast.makeText(this, "Email inválido", Toast.LENGTH_SHORT).show()
            }


            // Animação para imagem7
            imagem7.alpha = 0f
            imagem7.animate()
                .alpha(1f)
                .setDuration(1000)
                .withStartAction {
                    imagem7.visibility = ImageView.VISIBLE
                }
                .start()

            // Animação para imagem8 (inicia depois de 1 segundo)
            imagem8.alpha = 0f
            imagem8.postDelayed({
                imagem8.animate()
                    .alpha(1f)
                    .setDuration(1000)
                    .withStartAction {
                        imagem8.visibility = ImageView.VISIBLE
                    }
                    .start()
            }, 1000)
        }
    }

    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.(com|com\\.br)"
        return email.matches(emailPattern.toRegex())
    }


    private fun setupFormSenhalog(){
        val imagem11 = findViewById<ImageView>(R.id.imageView8)
        val btnNext = findViewById<ImageButton>(R.id.bntpg6) //coloca o botão aqui
        val btnNextToReturn = findViewById<ImageButton>(R.id.return6) //coloca o botão aqui
        val txtSenha = findViewById<EditText>(R.id.txtSenhaLog)

        btnNextToReturn.setOnClickListener {
            setContentView(R.layout.activity_main5) //chamando a página que vai aparecer
            speak("Certo!Vamos fazer o login. Agora digita seu email.")

            setupFormLogin() //chama a função pro form3 ai tem que colocar o resto das coisas
        }

        btnNext.setOnClickListener {
            val senha = txtSenha.text.toString()

            if (emailTemp.isNotEmpty() && senha.isNotEmpty()) {
                // Consulta o Firestore para verificar se o email e a senha correspondem
                val db = FirebaseFirestore.getInstance()
                val usuariosRef = db.collection("Users")

                usuariosRef.whereEqualTo("email", emailTemp)
                    .whereEqualTo("senha", senha)
                    .get()
                    .addOnSuccessListener { documents ->
                        if (!documents.isEmpty) {
                            // O email e a senha correspondem, avance para a próxima etapa
                            setContentView(R.layout.activity_main9)
                            setupFormAtendimento()

                            speak(" Escolha um atendimento! Vamos emitir a senha.")
                        } else {
                            // E-mail ou senha incorretos, exiba uma mensagem de erro
                            Toast.makeText(this, "E-mail ou senha incorretos", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .addOnFailureListener { e ->
                        // Trate os erros de consulta, se necessário
                        Toast.makeText(this, "Erro ao verificar o e-mail e a senha", Toast.LENGTH_SHORT).show()
                    }
            } else {
                // Exiba uma mensagem de erro se o e-mail ou a senha forem inválidos
                Toast.makeText(this, "E-mail ou senha inválidos", Toast.LENGTH_SHORT).show()
            }
        }





        imagem11.alpha = 0f
        imagem11.animate()
            .alpha(1f)
            .setDuration(1000)
            .withStartAction {
                imagem11.visibility = ImageView.VISIBLE
            }
            .start()



    }

    private fun setupFormCadFone() {


        val imagem14 = findViewById<ImageView>(R.id.imageView23)
        val imagem15 = findViewById<ImageView>(R.id.imageView24)
        val btnNext = findViewById<ImageButton>(R.id.bntpg8) //coloca o botão aqui
        val btnNextToReturn = findViewById<ImageButton>(R.id.return8) //coloca o botão aqui
        val txtTelefone = findViewById<EditText>(R.id.txtTelefone)
        speak(" Muito bem!Qual seu telefone?")

        btnNextToReturn.setOnClickListener {
            setContentView(R.layout.activity_main4) // chamando a página que vai aparecer



            setupFormCadEmail() // chama a função pro form3 ai tem que colocar o resto das coisas
        }

        btnNext.setOnClickListener {
            val telefoneEditText = findViewById<EditText>(R.id.txtTelefone)
            val telefone = telefoneEditText.text.toString()

            if (isValidPhoneNumber(telefone)) {
                usuarioTemp.telefone = telefone




                // Avance para a próxima etapa (coleta de senha)
                setContentView(R.layout.activity_main7)


                setupFormCadSenha()


            } else {
                // Exiba uma mensagem de erro se o telefone for inválido
                Toast.makeText(this, "Telefone inválido", Toast.LENGTH_SHORT).show()
            }
        }


    // Adicione um TextWatcher ao EditText do telefone
        txtTelefone.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Não é necessário implementar isso
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Não é necessário implementar isso
            }

            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    val cleanedText = it.toString().replace(Regex("[^0-9]"), "")

                    if (cleanedText != it.toString()) {
                        var formattedText = cleanedText

                        if (cleanedText.length >= 2) {
                            // Insere o primeiro parêntese após o segundo dígito
                            formattedText = formattedText.substring(0, 2) + " (" + formattedText.substring(2)
                        }

                        if (cleanedText.length >= 3) {
                            // Insere o segundo parêntese após o terceiro dígito
                            formattedText = formattedText.substring(0, 6) + ") " + formattedText.substring(6)
                        }

                        txtTelefone.setText(formattedText)
                        txtTelefone.setSelection(formattedText.length)
                    }
                }
            }
        })

        // Animação para imagem14
        imagem14.alpha = 0f
        imagem14.animate()
            .alpha(1f)
            .setDuration(1000)
            .withStartAction {
                imagem14.visibility = ImageView.VISIBLE
            }
            .start()

        // Animação para imagem15 (inicia depois de 1 segundo)
        imagem15.alpha = 0f
        imagem15.postDelayed({
            imagem15.animate()
                .alpha(1f)
                .setDuration(1000)
                .withStartAction {
                    imagem15.visibility = ImageView.VISIBLE
                }
                .start()
        }, 1000)
    }

    private fun isValidPhoneNumber(phone: String): Boolean {
        // Remova todos os caracteres que não são números
        val cleanedPhone = phone.replace(Regex("[^0-9]"), "")

        // Verifique se o número de telefone tem pelo menos 10 dígitos
        return cleanedPhone.length >= 10
    }



    private fun setupFormCadSenha() {


        val imagem12 = findViewById<ImageView>(R.id.imageView18)
        val imagem13 = findViewById<ImageView>(R.id.imageView21)
        val btnNext = findViewById<ImageButton>(R.id.bntpg7) //coloca o botão aqui
        val btnNextToReturn = findViewById<ImageButton>(R.id.return7) //coloca o botão aqui
        speak("Vamos criar uma senha? Quase acabando")


        btnNextToReturn.setOnClickListener {
            setContentView(R.layout.activity_main8) // chamando a página que vai aparecer


            setupFormCadFone() // chama a função pro form3 ai tem que colocar o resto das coisas
        }



        btnNext.setOnClickListener {
            val senhaEditText = findViewById<EditText>(R.id.txtSenha)
            val senha = senhaEditText.text.toString()

            if (senha.isNotEmpty()) {
                usuarioTemp.senha = senha
                val firebaseFirestore = FirebaseFirestore.getInstance()
                val collection = firebaseFirestore.collection("Users")

                collection.add(usuarioTemp)
                    .addOnSuccessListener { documentReference ->
                        // Documento adicionado com sucesso
                        val novoUsuarioID = documentReference.id
                        // Avance para a próxima etapa
                        setContentView(R.layout.activity_main9)


                        setupFormAtendimento()
                    }
                    .addOnFailureListener { e ->
                        // Ocorreu um erro ao adicionar o documento
                        // Exiba uma mensagem de erro
                    }

            } else {
                // Exiba um aviso se a senha não foi digitada
                Toast.makeText(this, "Por favor, digite a senha.", Toast.LENGTH_SHORT).show()
            }



            // Animação para imagem12
            imagem12.alpha = 0f
            imagem12.animate()
                .alpha(1f)
                .setDuration(1000)
                .withStartAction {
                    imagem12.visibility = ImageView.VISIBLE
                }
                .start()

            // Animação para imagem13 (inicia depois de 1 segundo)
            imagem13.alpha = 0f
            imagem13.postDelayed({
                imagem13.animate()
                    .alpha(1f)
                    .setDuration(1000)
                    .withStartAction {
                        imagem13.visibility = ImageView.VISIBLE
                    }
                    .start()
            }, 1000)
        }
    }

    private var numeroSala = 0
    private var numeroGerado = 0
    private var numeroSenhaChamada = 0



    private fun setupFormAtendimento(){
        val imagem16 = findViewById<ImageView>(R.id.imageView26)
        val imagem17 = findViewById<ImageView>(R.id.imageView27)
        val btnMedGeral = findViewById<ImageButton>(R.id.bntMed) //coloca o botão aqui
        val btnPedi = findViewById<ImageButton>(R.id.bntPedi) //coloca o botão aqui
        val btnOrtop = findViewById<ImageButton>(R.id.bntOrto) //coloca o botão aqui
        val btnNextToReturn = findViewById<ImageButton>(R.id.return10) //coloca o botão aqui
        speak(" Escolha um atendimento!Vamos emitir a senha.")


        btnNextToReturn.setOnClickListener {

            setContentView(R.layout.activity_main2) //chamando a página que vai aparecer
            setupFormCadSenha() //chama a função pro form3 ai tem que colocar o resto das coisas
        }


        btnMedGeral.setOnClickListener {
            numeroSala = 2 // Define o número da sala para Pedi
            numeroGerado++
            exibirNumerosSalaEspera()
            setContentView(R.layout.activity_main10) //chamando a página que vai aparecer


            setupFormSalaEspera() //chama a função pro form3 ai tem que colocar o resto das coisas

        }

        btnPedi.setOnClickListener {
            numeroSala = 3 // Define o número da sala para Ortop
            numeroGerado++
            exibirNumerosSalaEspera()
            setContentView(R.layout.activity_main10) //chamando a página que vai aparecer

            setupFormSalaEspera() //chama a função pro form3 ai tem que colocar o resto das coisas
        }

        btnOrtop.setOnClickListener {
            numeroSala = 1 // Define o número da sala para MedGeral
            numeroGerado++ // Incrementa o número gerado
            exibirNumerosSalaEspera()
            setContentView(R.layout.activity_main10) //chamando a página que vai aparecer
            setupFormSalaEspera() //chama a função pro form3 ai tem que colocar o resto das coisas
        }




        // Animação para imagem16
        imagem16.alpha = 0f
        imagem16.animate()
            .alpha(1f)
            .setDuration(1000)
            .withStartAction {
                imagem16.visibility = ImageView.VISIBLE
            }
            .start()

        // Animação para imagem17 (inicia depois de 1 segundo)
        imagem17.alpha = 0f
        imagem17.postDelayed({
            imagem17.animate()
                .alpha(1f)
                .setDuration(1000)
                .withStartAction {
                    imagem17.visibility = ImageView.VISIBLE
                }
                .start()
        }, 1000)


    }

    private fun construirConteudoEmail(): String {
        val timeStamp = SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.getDefault()).format(Date())
        val conteudo = StringBuilder()
        conteudo.append("Histórico de Senhas e Salas - $timeStamp\n\n")
        conteudo.append("Número de Senhas Chamadas: $numeroSenhaChamada\n")
        conteudo.append("Histórico de Salas:\n")

        for (i in 1..numeroSenhaChamada) {
            conteudo.append("Senha $i: Sala ${obterProximaSala()}\n")
        }

        return conteudo.toString()
    }

    private fun enviarEmail(conteudoEmail: String) {
        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.type = "plain/text"
        emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("thiagokroos21@gmail.com.com"))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Histórico de Senhas e Salas")
        emailIntent.putExtra(Intent.EXTRA_TEXT, conteudoEmail)

        try {
            startActivity(Intent.createChooser(emailIntent, "Enviar email..."))
        } catch (ex: android.content.ActivityNotFoundException) {
            Toast.makeText(this, "Nenhum cliente de e-mail instalado.", Toast.LENGTH_SHORT).show()
        }
    }




    private fun exibirNumerosSalaEspera() {
        setContentView(R.layout.activity_main10) // Altera o layout para "Sala de Espera"

        val txtSenhaEspera = findViewById<TextView>(R.id.Senha)

        // Exibe o número da sala e o número gerado nas TextViews da "Sala de Espera"
        txtSenhaEspera.text = " $numeroGerado"
        salaAtualIndex = 0 // Reinicia o índice da sala atual
    }



    private var salas = listOf(1, 2, 3).shuffled() // Salas em ordem aleatória
    private var salaAtualIndex = 0 // Índice da sala atual
    private var chamadasPorSala = 0 // Número de chamadas por sala


    private var ultimaSalaChamada = 0 // Armazena a última sala chamada

    private fun obterProximaSala(): Int {
        val proximaSala = salas[salaAtualIndex]

        salaAtualIndex = (salaAtualIndex + 1) % salas.size // Avança para a próxima sala, ciclo se atingir o final

        return proximaSala
    }

    private fun setupFormSalaEspera() {
        exibirNumerosSalaEspera()
        val imagem18 = findViewById<ImageView>(R.id.imageView29)
        val imagem19 = findViewById<ImageView>(R.id.imageView30)
        speak("Vamos te chamar por aqui!")

        val btnNextToReturn = findViewById<ImageButton>(R.id.return10) // coloca o botão aqui

        btnNextToReturn.setOnClickListener {
            val conteudoEmail = construirConteudoEmail()
            enviarEmail(conteudoEmail)

            setContentView(R.layout.activity_main2)
            setupFormEscolha() // chama a função pro form3 ai tem que colocar o resto das coisas
        }

        val handlerSenha = Handler()
        val handlerSala = Handler()
        val horizontalScrollViewSenha = findViewById<HorizontalScrollView>(R.id.horizontalScrollView)
        val horizontalScrollViewSala = findViewById<HorizontalScrollView>(R.id.horizontalScrollViewSala)
        val senhaLayout = findViewById<LinearLayout>(R.id.senhaLayout)
        val salaLayout = findViewById<LinearLayout>(R.id.salaLayout)

        val delaySenha = 5000L

        val delaySala = 5000L

        val senhaChamadaRunnable = object : Runnable {
            override fun run() {
                if (numeroSenhaChamada < 100) {
                    numeroSenhaChamada++
                    // Adicione a senha chamada ao layout horizontal
                    val senhaChamadaView = TextView(this@MainActivity)
                    senhaChamadaView.text = "  $numeroSenhaChamada            "

                    senhaChamadaView.textSize = 32f
                    senhaChamadaView.setPadding(330, 0, 10, 150)
                    senhaLayout.addView(senhaChamadaView)

                    // Remove a senha após um atraso
                    handlerSenha.postDelayed({
                        senhaLayout.removeView(senhaChamadaView)
                    }, delaySenha)
                }
                horizontalScrollViewSenha.fullScroll(View.FOCUS_RIGHT)
                handlerSenha.postDelayed(this, delaySenha)
            }
        }

        val salaChamadaRunnable = object : Runnable {
            override fun run() {
                // Adicione a sala chamada ao layout horizontal
                val proximaSala = obterProximaSala()
                val salaChamadaView = TextView(this@MainActivity)
                salaChamadaView.text = " Sala $proximaSala       "

                salaChamadaView.textSize = 24f
                salaChamadaView.setPadding(300, 0, 10, 150)
                salaLayout.addView(salaChamadaView)

                // Remove a sala após um atraso
                handlerSala.postDelayed({
                    salaLayout.removeView(salaChamadaView)
                }, delaySala)

                horizontalScrollViewSala.fullScroll(View.FOCUS_RIGHT)
                handlerSala.postDelayed(this, delaySala)
            }
        }

        handlerSenha.postDelayed(senhaChamadaRunnable, delaySenha)
        handlerSala.postDelayed(salaChamadaRunnable, delaySala)


        // Animação para imagem18
        imagem18.alpha = 0f
        imagem18.animate()
            .alpha(1f)
            .setDuration(1000)
            .withStartAction {
                imagem18.visibility = ImageView.VISIBLE
            }
            .start()

        // Animação para imagem19 (inicia depois de 1 segundo)
        imagem19.alpha = 0f
        imagem19.postDelayed({
            imagem19.animate()
                .alpha(1f)
                .setDuration(1000)
                .withStartAction {
                    imagem19.visibility = ImageView.VISIBLE
                }
                .start()
        }, 1000)
    }

}