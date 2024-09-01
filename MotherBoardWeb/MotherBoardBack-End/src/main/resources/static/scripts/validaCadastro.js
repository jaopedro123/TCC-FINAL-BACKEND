const fullNameInput = document.getElementById('fullName');
const cpfInput = document.getElementById('cpf');
const emailInput = document.getElementById('email');
const phoneInput = document.getElementById('phone');
const passwordInput = document.getElementById('password');
const confirmPasswordInput = document.getElementById('confirmPassword');
const checkboxInput = document.getElementById('termsCheckbox');

const fullNameError = document.getElementById('fullNameError');
const cpfError = document.getElementById('cpfError');
const emailError = document.getElementById('emailError');
const phoneError = document.getElementById('phoneError');
const passwordError = document.getElementById('passwordError');
const confirmPasswordError = document.getElementById('confirmPasswordError');
const checkboxError = document.querySelector('.error-message');

/* Cadastro ativo inativo */
const btnPessoaFisica = document.getElementById('btnPessoaFisica')
const btnPessoaJuridica = document.getElementById('btnPessoaJuridica')
const registrationFormFisica = document.getElementById('registrationFormFisica')
const registrationFormJuridica = document.getElementById('registrationFormJuridica')

function pessoaFisicaAtivo() {
  registrationFormFisica.style.display = "block"
  registrationFormJuridica.style.display = "none"

    btnPessoaJuridica.classList.remove('active');
    btnPessoaFisica.classList.add('active');
}

function pessoaJuridicoAtivo() {
  registrationFormFisica.style.display = "none"
  registrationFormJuridica.style.display = "block"

    btnPessoaFisica.classList.remove('active');
    btnPessoaJuridica.classList.add('active');
}
/* fim - Cadastro ativo inativo */

// Função para validar Nome Completo
function validateFullName() {
    const namePattern = /^[a-zA-Z\s]+$/;
    if (fullNameInput.value.trim() === "") {
        fullNameError.textContent = "Preencha este campo.";
        fullNameError.style.display = "block";
        fullNameInput.classList.add('is-invalid');
    } else if (fullNameInput.value.trim().length < 3) {
        fullNameError.textContent = "O nome deve conter pelo menos 3 caracteres.";
        fullNameError.style.display = "block";
        fullNameInput.classList.add('is-invalid');
    } else if (!namePattern.test(fullNameInput.value)) {
        fullNameError.textContent = "O nome deve conter apenas letras.";
        fullNameError.style.display = "block";
        fullNameInput.classList.add('is-invalid');
    } else {
        fullNameError.style.display = "none";
        fullNameInput.classList.remove('is-invalid');
        fullNameInput.classList.add('is-valid');
    }
}

// Função para mascara de CPF
function formatCPF() {
    let cpfValue = cpfInput.value.replace(/\D/g, '');
    if (cpfValue.length > 0) {
        cpfValue = cpfValue.replace(/(\d{3})(\d)/, "$1.$2");
        cpfValue = cpfValue.replace(/(\d{3})(\d)/, "$1.$2");
        cpfValue = cpfValue.replace(/(\d{3})(\d{2})$/, "$1-$2");
    }
    cpfInput.value = cpfValue;
}

// API para validar CPF
function isCpf(cpf) {
    cpf = cpf.replace(/\D/g, '');
    if (cpf.length != 11 || /^(\d)\1{10}$/.test(cpf)) return false;
    let result = true;
    [9, 10].forEach(function(j) {
        let soma = 0, r;
        cpf.split(/(?=)/).splice(0, j).forEach(function(e, i) {
            soma += parseInt(e) * ((j + 2) - (i + 1));
        });
        r = soma % 11;
        r = (r < 2) ? 0 : 11 - r;
        if (r != cpf.substring(j, j + 1)) result = false;
    });
    return result;
}

// Validação de CPF com a nova função
function validateCPF() {
    if (cpfInput.value.trim() === "") {
        cpfError.textContent = "Preencha este campo.";
        cpfError.style.display = "block";
        cpfInput.classList.add('is-invalid');
    } else if (!isCpf(cpfInput.value)) {
        cpfError.textContent = "CPF inválido.";
        cpfError.style.display = "block";
        cpfInput.classList.add('is-invalid');
    } else {
        cpfError.style.display = "none";
        cpfInput.classList.remove('is-invalid');
        cpfInput.classList.add('is-valid');
    }
}

// Função para validar E-mail
function validateEmail() {
    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (emailInput.value.trim() === "") {
        emailError.textContent = "Preencha este campo.";
        emailError.style.display = "block";
        emailInput.classList.add('is-invalid');
    } else if (!emailPattern.test(emailInput.value)) {
        emailError.textContent = "E-mail inválido.";
        emailError.style.display = "block";
        emailInput.classList.add('is-invalid');
    } else {
        emailError.style.display = "none";
        emailInput.classList.remove('is-invalid');
        emailInput.classList.add('is-valid');
    }
}

/*
// Função para validar E-mail com a API
function validateEmail() {
    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (emailInput.value.trim() === "") {
        emailError.textContent = "Preencha este campo.";
        emailError.style.display = "block";
        emailInput.classList.add('is-invalid');
    } else if (!emailPattern.test(emailInput.value)) {
        emailError.textContent = "E-mail inválido.";
        emailError.style.display = "block";
        emailInput.classList.add('is-invalid');
    } else {
        // Validação adicional com a API
        validateAPIEmail();
    }
}
// Função para validar Email com API
function validateAPIEmail() {
    if (emailInput.value.trim() === "") {
        return;
    }

    //const apiKey = 'ab67c60948ea4ac2810add29d73b1881'; // Substitua pela sua chave de API 
    fetch(`https://api.zerobounce.net/v2/validate?api_key=d59f43ff77b84c16b28c762809cd47cf&email=${emailInput.value}`)
        .then(response => response.json())
        .then(data => {
            if (data.status === 'valid') {
                emailError.style.display = "none";
                emailInput.classList.remove('is-invalid');
                emailInput.classList.add('is-valid');
            } else {
                emailError.textContent = "E-mail inválido.";
                emailError.style.display = "block";
                emailInput.classList.add('is-invalid');
                emailInput.classList.remove('is-valid');
            }
        })
        .catch(error => {
            console.error('Erro ao verificar o email:', error);
        });
}
*/


// Função para formatar Telefone
function formatPhone() {
    let phoneValue = phoneInput.value.replace(/\D/g, '');
    if (phoneValue.length > 0) {
        if (phoneValue.length <= 10) {
            phoneValue = phoneValue.replace(/(\d{2})(\d)/, "($1) $2");
            phoneValue = phoneValue.replace(/(\d{4})(\d)/, "$1-$2");
        } else {
            phoneValue = phoneValue.replace(/(\d{2})(\d)/, "($1) $2");
            phoneValue = phoneValue.replace(/(\d{5})(\d)/, "$1-$2");
        }
    }
    phoneInput.value = phoneValue;
}

// Função para validar Telefone
function validatePhone() {
    const phonePattern = /^\(\d{2}\) \d{5}-\d{4}$/;
    if (phoneInput.value.trim() === "") {
        phoneError.textContent = "Preencha este campo.";
        phoneError.style.display = "block";
        phoneInput.classList.add('is-invalid');
    } else if (!phonePattern.test(phoneInput.value)) {
        phoneError.textContent = "Telefone inválido.";
        phoneError.style.display = "block";
        phoneInput.classList.add('is-invalid');
    } else {
        phoneError.style.display = "none";
        phoneInput.classList.remove('is-invalid');
        phoneInput.classList.add('is-valid');
    }
}

// Função para validar Senha
function validatePassword() {
    const password = passwordInput.value;
    const hasUpperCase = /[A-Z]/.test(password);
    const hasLowerCase = /[a-z]/.test(password);
    const hasNumber = /\d/.test(password);
    const hasSpecialChar = /[!@#$%^&*(),.?":{}|<>]/.test(password);

    if (password === "") {
        passwordError.textContent = "Preencha este campo.";
        passwordError.style.display = "block";
        passwordInput.classList.add('is-invalid');
    } else if (password.length < 8) {
        passwordError.textContent = "A senha deve conter pelo menos 8 caracteres.";
        passwordError.style.display = "block";
        passwordInput.classList.add('is-invalid');
    } else if (!hasUpperCase || !hasLowerCase || !hasNumber) {
        passwordError.textContent = "A senha deve conter letra maiúscula, minúscula e um número.";
        passwordError.style.display = "block";
        passwordInput.classList.add('is-invalid');
    } else if (!hasSpecialChar) {
        passwordError.textContent = "A senha deve conter pelo menos um caractere especial.";
        passwordError.style.display = "block";
        passwordInput.classList.add('is-invalid');
    } else {
        passwordError.style.display = "none";
        passwordInput.classList.remove('is-invalid');
        passwordInput.classList.add('is-valid');
    }
    validateConfirmPassword();
}

// Função para validar Confirmar Senha
function validateConfirmPassword() {
    if (confirmPasswordInput.value.trim() === "") {
        confirmPasswordError.textContent = "Preencha este campo.";
        confirmPasswordError.style.display = "block";
        confirmPasswordInput.classList.add('is-invalid');
    } else if (confirmPasswordInput.value !== passwordInput.value) {
        confirmPasswordError.textContent = "As senhas não coincidem.";
        confirmPasswordError.style.display = "block";
        confirmPasswordInput.classList.add('is-invalid');
    } else if (passwordInput.classList.contains('is-invalid')) {
        confirmPasswordError.textContent = "A senha deve ser válida primeiro.";
        confirmPasswordError.style.display = "block";
        confirmPasswordInput.classList.add('is-invalid');
    } else {
        confirmPasswordError.style.display = "none";
        confirmPasswordInput.classList.remove('is-invalid');
        confirmPasswordInput.classList.add('is-valid');
    }
}

function validateCheckbox() {
    var checkboxInput = document.getElementById('termsCheckbox');
    var checkboxError = document.getElementById('checkboxError');
    if (!checkboxInput.checked) {
        checkboxError.style.display = "block";
        checkboxError.classList.add('d-block');
        checkboxError.textContent = "Você deve concordar com os termos para continuar.";
    } else {
        checkboxError.style.display = "none";
        checkboxError.classList.remove('d-block');
        checkboxError.textContent = "";
    }
}

function togglePasswordVisibility(inputId, icon) {
    const input = document.getElementById(inputId);
    const isPassword = input.type === "password";
    input.type = isPassword ? "text" : "password";
    icon.classList.toggle("bi-eye-slash-fill", isPassword);
    icon.classList.toggle("bi-eye-fill", !isPassword);
}

document.getElementById('togglePassword').addEventListener('click', function () {
    togglePasswordVisibility('password', this.firstElementChild);
});

document.getElementById('toggleConfirmPassword').addEventListener('click', function () {
    togglePasswordVisibility('confirmPassword', this.firstElementChild);
});

// Evento listeners
fullNameInput.addEventListener('input', validateFullName);
cpfInput.addEventListener('input', () => {
    formatCPF();
    validateCPF();
});
emailInput.addEventListener('input', () => {
    validateEmail();
    //validateAPIEmail(); chama o API
});
phoneInput.addEventListener('input', () => {
    formatPhone();
    validatePhone();
});
passwordInput.addEventListener('input', validatePassword);
confirmPasswordInput.addEventListener('input', validateConfirmPassword);
checkboxInput.addEventListener('change', validateCheckbox);

// Submit
registrationFormFisica.addEventListener('submit', function(event) {
    event.preventDefault();

    validateFullName();
    validateCPF();
    validateEmail();
    //validateAPIEmail();  chama o API
    validatePhone();
    validatePassword();
    validateConfirmPassword();
    validateCheckbox();

    const isFullNameValid = !fullNameInput.classList.contains('is-invalid');
    const isCPFValid = !cpfInput.classList.contains('is-invalid');
    const isEmailValid = !emailInput.classList.contains('is-invalid');
    const isPhoneValid = !phoneInput.classList.contains('is-invalid');
    const isPasswordValid = !passwordInput.classList.contains('is-invalid');
    const isConfirmPasswordValid = !confirmPasswordInput.classList.contains('is-invalid');
    const isCheckboxValid = checkboxInput.checked;

    if (isFullNameValid && isCPFValid && isEmailValid && isPhoneValid && isPasswordValid && isConfirmPasswordValid && isCheckboxValid) {
        alert("Cadastro realizado com sucesso!");
    }
});

// Evento listener para resetar
registrationFormFisica.addEventListener('reset', () => {
    fullNameError.style.display = "none";
    cpfError.style.display = "none";
    emailError.style.display = 'none';
    phoneError.style.display = "none";
    passwordError.style.display = 'none';
    confirmPasswordError.style.display = "none";
    fullNameInput.classList.remove('is-invalid', 'is-valid');
    cpfInput.classList.remove('is-invalid', 'is-valid');
    emailInput.classList.remove('is-invalid', 'is-valid');
    phoneInput.classList.remove('is-invalid', 'is-valid');
    passwordInput.classList.remove('is-invalid', 'is-valid');
    confirmPasswordInput.classList.remove('is-invalid', 'is-valid');
});


