document.addEventListener("DOMContentLoaded", function () {
    // Seleção de elementos do formulário
    const nomeInput = document.getElementById("nomeCompleto");
    const nomeError = document.getElementById("fullNameError");
    const form = document.querySelector(".needs-validation");
    const passwordInput = document.getElementById('senha');
    const confirmPasswordInput = document.getElementById('confirmpassword');
    const senhaInv = document.getElementById('senhaInv');
    const confirmarSenhaInv = document.getElementById('confirmarSenhaInv');

    // Validação personalizada para o campo de nome completo
    nomeInput.addEventListener("input", function () {
        if (nomeInput.value.trim().length < 3) { // Considerar apenas caracteres reais
            nomeInput.classList.add("is-invalid");
            nomeError.style.display = "block";
        } else {
            nomeInput.classList.remove("is-invalid");
            nomeError.style.display = "none";
        }
    });

    // Validação dos campos de senha ao digitar
    passwordInput.addEventListener('input', validaSenha);
    confirmPasswordInput.addEventListener('input', validaConfirmarSenha);

    // Evento para validação no evento de submissão do formulário
    form.addEventListener("submit", function (event) {
        let isFormValid = true;

        // Verificar o campo de nome
        if (nomeInput.value.trim().length < 3) {
            nomeInput.classList.add("is-invalid");
            nomeError.style.display = "block";
            isFormValid = false;
        }

        // Verificar se o campo de senha é válido
        validaSenha();
        if (passwordInput.classList.contains("is-invalid")) {
            isFormValid = false;
        }

        // Verificar se o campo de confirmação de senha é válido
        validaConfirmarSenha();
        if (confirmPasswordInput.classList.contains("is-invalid")) {
            isFormValid = false;
        }

        // Impedir envio se o formulário não atender aos critérios mínimos
        if (!isFormValid || !form.checkValidity()) {
            event.preventDefault();
            event.stopPropagation();
        } else {
            form.classList.add('was-validated'); // Adicionar a classe se tudo estiver válido
        }
    });

    // Função para validar a senha
    function validaSenha() {
        const password = passwordInput.value;
        const hasUpperCase = /[A-Z]/.test(password);
        const hasLowerCase = /[a-z]/.test(password);
        const hasNumber = /\d/.test(password);
        const hasSpecialChar = /[!@#$%^&*(),.?":{}|<>]/.test(password);

        if (password === "") {
            removerQndInputVazio(passwordInput, senhaInv);
        } else if (password.length < 8) {
            senhaInv.textContent = "A senha deve conter pelo menos 8 caracteres.";
            invalidar(passwordInput, senhaInv);
        } else if (!hasUpperCase || !hasLowerCase || !hasNumber) {
            senhaInv.textContent = "A senha deve conter letra maiúscula, minúscula e um número.";
            invalidar(passwordInput, senhaInv);
        } else if (!hasSpecialChar) {
            senhaInv.textContent = "A senha deve conter pelo menos um caractere especial.";
            invalidar(passwordInput, senhaInv);
        } else {
            validar(passwordInput, senhaInv);
        }

        validaConfirmarSenha(); // Revalidar a confirmação de senha sempre que a senha principal mudar
    }

    // Função para validar a confirmação de senha
    function validaConfirmarSenha() {
        if (confirmPasswordInput.value !== passwordInput.value) {
            confirmarSenhaInv.textContent = "As senhas não coincidem.";
            invalidar(confirmPasswordInput, confirmarSenhaInv);
        } else if (passwordInput.classList.contains('is-invalid')) {
            confirmarSenhaInv.textContent = "A senha deve ser válida primeiro.";
            invalidar(confirmPasswordInput, confirmarSenhaInv);
        } else if (confirmPasswordInput.value === "") {
            removerQndInputVazio(confirmPasswordInput, confirmarSenhaInv);
        } else {
            validar(confirmPasswordInput, confirmarSenhaInv);
        }
    }

    // Funções auxiliares para manipular as classes de validação e as mensagens de erro
    function removerQndInputVazio(input, idInv) {
        idInv.style.display = "none";
        input.classList.remove('is-invalid');
        input.classList.remove('is-valid');
    }

    function validar(input, id) {
        input.classList.remove('is-invalid');
        input.classList.add('is-valid');
        id.style.display = 'none';
    }

    function invalidar(input, id) {
        input.classList.add('is-invalid');
        id.style.display = 'block';
    }
});
 

// Script da Imagem
$(document).ready(function () {
    $("#INTRAformConta").on("submit", function (event) {
        if ($("#senha").val() !== "") {
            if ($("#confirmpassword").hasClass("is-invalid")) {

                event.preventDefault();
            }
        }
    });

    $("#buttonCancel").on("click", function () {
        window.location = "/MotherBoardAdmin/";
    });

    $("#nomeCompleto").on("input", function () {
        this.value = this.value.replace(/[^a-zA-ZÀ-ÿ\s]/g, '');
    });

$("#foto").change(function () {
    var file = this.files[0].size;

    // Limitar o tamanho da imagem a 1 MB
    if (file > 1048576) {
        alert("nao pode mais de 1MB!");
    } else {
        this.setCustomValidity("");
        showImageThumbnail(this);
    }
});


    document.getElementById('thumbnail').addEventListener('click', function () {
        document.getElementById('foto').click();
    });

});

function showImageThumbnail(fileInput) {
    var file = fileInput.files[0];
    var reader = new FileReader();
    reader.onload = function (e) {
        $("#thumbnail").attr("src", e.target.result);
    };
    reader.readAsDataURL(file);
}



// Mostrar/Ocultar Senha
function togglePasswordVisibility(inputId, icon) {
    const input = document.getElementById(inputId);
    const isPassword = input.type === "password";
    input.type = isPassword ? "text" : "password";
    icon.classList.toggle("bi-eye-slash-fill", isPassword);
    icon.classList.toggle("bi-eye-fill", !isPassword);
}

document.getElementById('togglePassword').addEventListener('click', function () {
    togglePasswordVisibility('senha', this.firstElementChild);
});

function togglePasswordVisibility(inputId, icon) {
    const input = document.getElementById(inputId);
    const isPasswordType = input.type === "password"; 
    input.type = isPasswordType ? "text" : "password"; 
    icon.classList.toggle("bi-eye-slash-fill", isPasswordType);
    icon.classList.toggle("bi-eye-fill", !isPasswordType);
}

// Evento para alternar a visibilidade no botão de confirmação de senha
document.getElementById('togglePassword2').addEventListener('click', function () {
    togglePasswordVisibility('confirmpassword', this.firstElementChild);
});