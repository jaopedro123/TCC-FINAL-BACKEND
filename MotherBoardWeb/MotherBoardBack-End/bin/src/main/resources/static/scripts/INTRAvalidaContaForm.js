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

        foto = this.files[0].size;

        // 1 MB = 1048576
        if (foto > 1048576) {
            this.setCustomValidity("Você só pode escolher imagens abaixo de 1 MB! ");
            this.reportvalidity();
        }
        else {
            this.setCustomValidity("");
            showImageThumbnail(this);
        }
    });

    document.getElementById('thumbnail').addEventListener('click', function () {
        document.getElementById('foto').click(); // Simula o clique no input de arquivo
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

const passwordInput = document.getElementById('senha');
const confirmPasswordInput = document.getElementById('confirmpassword');
const senhaInv = document.getElementById('senhaInv');
const confirmarSenhaInv = document.getElementById('confirmarSenhaInv');

function validaSenha() {
    const password = passwordInput.value;
    const hasUpperCase = /[A-Z]/.test(password);
    const hasLowerCase = /[a-z]/.test(password);
    const hasNumber = /\d/.test(password);
    const hasSpecialChar = /[!@#$%^&*(),.?":{}|<>]/.test(password);

    if (password === "") {
        removerQndInputVazio(passwordInput, senhaInv);
    } else if (password !== "" && password.length < 8) {
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

    validaConfirmarSenha();
}

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
        validar(confirmPasswordInput, confirmarSenhaInv)
    }
}

function removerQndInputVazio(input , idInv) {
    idInv.style.display = "none";
    input.classList.remove('is-invalid');
    input.classList.remove('is-valid');
}

function validar(input, id) {
    input.classList.remove('is-invalid')
    input.classList.add('is-valid')
    id.style.display = 'none'
}

function invalidar(input, id) {
    input.classList.add('is-invalid')
    id.style.display = 'block';
}
