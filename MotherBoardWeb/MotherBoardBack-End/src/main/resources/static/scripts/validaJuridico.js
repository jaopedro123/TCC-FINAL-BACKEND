const fantasyNameInput = document.getElementById('fantasyName');
const razaosocialInput = document.getElementById('razaosocial');
const inscricaoEstadualInput = document.getElementById('inscricaoestadual');
const cnpjInput = document.getElementById('cnpj');
const emailInput2 = document.getElementById('email2');
const phoneInput2 = document.getElementById('phone2');
const passwordInput2 = document.getElementById('password2');
const confirmPasswordInput2 = document.getElementById('confirmPassword2');
const checkboxInput2 = document.getElementById('termsCheckbox2');

const fantasyNameError = document.getElementById('fantasyNameError');
const razaosocialError = document.getElementById('razaosocialError');
const inscricaoEstadualError = document.getElementById('inscricaoestadualError');
const cnpjError = document.getElementById('cnpjError');
const emailError2 = document.getElementById('emailError2');
const phoneError2 = document.getElementById('phoneError2');
const passwordError2 = document.getElementById('passwordError2');
const confirmPasswordError2 = document.getElementById('confirmPasswordError2');
const checkboxInscricaoEstadual = document.getElementById('flexCheckDefault');
const checkboxError2 = document.querySelector('.error-message');



// Validate Nome Fantasia
function validateFantasyName() {
    if (fantasyNameInput.value.trim() === "") {
        fantasyNameError.textContent = "Preencha este campo.";
        fantasyNameError.style.display = "block";
        fantasyNameInput.classList.add('is-invalid');
    }else if (fantasyNameInput.value.trim().length < 3) {
        fantasyNameError.textContent = "O nome fantasia deve conter pelo menos 3 caracteres.";
        fantasyNameError.style.display = "block";
        fantasyNameInput.classList.add('is-invalid');
    } else {
        fantasyNameError.style.display = "none";
        fantasyNameInput.classList.remove('is-invalid');
        fantasyNameInput.classList.add('is-valid');
    }
}

// Validate Razão Social
function validateRazaoSocial() {
    if (razaosocialInput.value.trim() === "") {
        razaosocialError.textContent = "Preencha este campo.";
        razaosocialError.style.display = "block";
        razaosocialInput.classList.add('is-invalid');
    }else if (razaosocialInput.value.trim().length < 3) {
        razaosocialError.textContent = "O nome fantasia deve conter pelo menos 3 caracteres.";
        razaosocialError.style.display = "block";
        razaosocialInput.classList.add('is-invalid');
    } else {
        razaosocialError.style.display = "none";
        razaosocialInput.classList.remove('is-invalid');
        razaosocialInput.classList.add('is-valid');
    }
}

function validateInscricaoEstadual() {
    if (checkboxInscricaoEstadual.checked) {
        inscricaoEstadualError.style.display = "none";
        inscricaoEstadualInput.classList.remove('is-invalid');
        inscricaoEstadualInput.classList.remove('is-valid');
        return; // Interrompe a validação
    }

    if (inscricaoEstadualInput.value.trim() === "") {
        inscricaoEstadualError.textContent = "Preencha este campo.";
        inscricaoEstadualError.style.display = "block";
        inscricaoEstadualInput.classList.add('is-invalid');
    } else if (!/^\d{9}$/.test(inscricaoEstadualInput.value.trim())) {
        inscricaoEstadualError.textContent = "Inscrição Estadual deve conter 9 dígitos.";
        inscricaoEstadualError.style.display = "block";
        inscricaoEstadualInput.classList.add('is-invalid');
    } else {
        inscricaoEstadualError.style.display = "none";
        inscricaoEstadualInput.classList.remove('is-invalid');
        inscricaoEstadualInput.classList.add('is-valid');
    }
}


// Apenas números
function formatInscricaoEstadual() {
    inscricaoEstadualInput.value = inscricaoEstadualInput.value.replace(/\D/g, '');
}

// Evento listener para Inscrição Estadual
inscricaoEstadualInput.addEventListener('input', () => {
    formatInscricaoEstadual();
    validateInscricaoEstadual();
});



/* Mascara CNPJ*/
function formatCNPJ() {
    let cnpjValue = cnpjInput.value.replace(/\D/g, '');

    if (cnpjValue.length > 14) {
        cnpjValue = cnpjValue.slice(0, 14);
    }

    if (cnpjValue.length > 12) {
        cnpjValue = cnpjValue.replace(/(\d{2})(\d{3})(\d{3})(\d{4})(\d{2})/, "$1.$2.$3/$4-$5");
    } else if (cnpjValue.length > 6) {
        cnpjValue = cnpjValue.replace(/(\d{2})(\d{3})(\d{3})/, "$1.$2.$3");
    } else if (cnpjValue.length > 2) {
        cnpjValue = cnpjValue.replace(/(\d{2})(\d)/, "$1.$2");
    }

    cnpjInput.value = cnpjValue;
}

cnpjInput.addEventListener('input', () => {
    formatCNPJ();
    validateCNPJ();
});

// Validação CNPJ
function validateCNPJ() {
    if (cnpjInput.value.trim() === "") {
        cnpjError.textContent = "Preencha este campo.";
        cnpjError.style.display = "block";
        cnpjInput.classList.add('is-invalid');
    } else if (!isCnpj(cnpjInput.value.replace(/\D/g, ''))) {
        cnpjError.textContent = "CNPJ inválido.";
        cnpjError.style.display = "block";
        cnpjInput.classList.add('is-invalid');
    } else {
        cnpjError.style.display = "none";
        cnpjInput.classList.remove('is-invalid');
        cnpjInput.classList.add('is-valid');
    }
}

// API CNPJ 
function isCnpj(cnpj) {
    // Remove caracteres não numéricos
    cnpj = cnpj.replace(/[^\d]/g, '');

    if (cnpj.length !== 14) return false;

    // Validação dos dígitos verificadores
    let tamanho = cnpj.length - 2;
    let numeros = cnpj.substring(0, tamanho);
    let digitos = cnpj.substring(tamanho);
    let soma = 0;
    let pos = tamanho - 7;

    for (let i = tamanho; i >= 1; i--) {
        soma += numeros.charAt(tamanho - i) * pos--;
        if (pos < 2) pos = 9;
    }

    let resultado = soma % 11 < 2 ? 0 : 11 - (soma % 11);
    if (resultado != digitos.charAt(0)) return false;

    tamanho = tamanho + 1;
    numeros = cnpj.substring(0, tamanho);
    soma = 0;
    pos = tamanho - 7;

    for (let i = tamanho; i >= 1; i--) {
        soma += numeros.charAt(tamanho - i) * pos--;
        if (pos < 2) pos = 9;
    }

    resultado = soma % 11 < 2 ? 0 : 11 - (soma % 11);
    return resultado == digitos.charAt(1);
}

// Função para validar E-mail
function validateEmail2() {
    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (emailInput2.value.trim() === "") {
        emailError2.textContent = "Preencha este campo.";
        emailError2.style.display = "block";
        emailInput2.classList.add('is-invalid');
    } else if (!emailPattern.test(emailInput2.value)) {
        emailError2.textContent = "E-mail inválido.";
        emailError2.style.display = "block";
        emailInput2.classList.add('is-invalid');
    } else {
        emailError2.style.display = "none";
        emailInput2.classList.remove('is-invalid');
        emailInput2.classList.add('is-valid');
    }
}

/*
// Função para validar E-mail
function validateEmail2() {
    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (emailInput2.value.trim() === "") {
        emailError2.textContent = "Preencha este campo.";
        emailError2.style.display = "block";
        emailInput2.classList.add('is-invalid');
    } else if (!emailPattern.test(emailInput2.value)) {
        emailError2.textContent = "E-mail inválido.";
        emailError2.style.display = "block";
        emailInput2.classList.add('is-invalid');
    } else {
        // Validação adicional com a API
        validateAPIEmail2();
    }
}

// Função para validar Email com API
function validateAPIEmail2() {
    if (emailInput2.value.trim() === "") {
        return;
    }

    //const apiKey = 'ab67c60948ea4ac2810add29d73b1881'; // Substitua pela sua chave de API 
    fetch(`https://api.zerobounce.net/v2/validate?api_key=d59f43ff77b84c16b28c762809cd47cf&email=${emailInput2.value}`)
        .then(response => response.json())
        .then(data => {
            if (data.status === 'valid') {
                emailError2.style.display = "none";
                emailInput2.classList.remove('is-invalid');
                emailInput2.classList.add('is-valid');
            } else {
                emailError2.textContent = "E-mail inválido.";
                emailError2.style.display = "block";
                emailInput2.classList.add('is-invalid');
                emailInput2.classList.remove('is-valid');
            }
        })
        .catch(error => {
            console.error('Erro ao verificar o email:', error);
        });
}
*/

// Validar Telefone
function formatPhone2() {
    let phoneValue = phoneInput2.value.replace(/\D/g, '');
    if (phoneValue.length > 0) {
        if (phoneValue.length <= 10) {
            phoneValue = phoneValue.replace(/(\d{2})(\d)/, "($1) $2");
            phoneValue = phoneValue.replace(/(\d{4})(\d)/, "$1-$2");
        } else {
            phoneValue = phoneValue.replace(/(\d{2})(\d)/, "($1) $2");
            phoneValue = phoneValue.replace(/(\d{5})(\d)/, "$1-$2");
        }
    }
    phoneInput2.value = phoneValue;
}

function validatePhone2() {
    const phonePattern = /^\(\d{2}\) \d{4,5}-\d{4}$/;
    if (phoneInput2.value.trim() === "") {
        phoneError2.textContent = "Preencha este campo.";
        phoneError2.style.display = "block";
        phoneInput2.classList.add('is-invalid');
    } else if (!phonePattern.test(phoneInput2.value)) {
        phoneError2.textContent = "Telefone inválido.";
        phoneError2.style.display = "block";
        phoneInput2.classList.add('is-invalid');
    } else {
        phoneError2.style.display = "none";
        phoneInput2.classList.remove('is-invalid');
        phoneInput2.classList.add('is-valid');
    }
}

// Validar Senha
function validatePassword2() {
    const password2 = passwordInput2.value;
    const hasUpperCase = /[A-Z]/.test(password2);
    const hasLowerCase = /[a-z]/.test(password2);
    const hasNumber = /\d/.test(password2);
    const hasSpecialChar = /[!@#$%^&*(),.?":{}|<>]/.test(password2);

    if (password2 === "") {
        passwordError2.textContent = "Preencha este campo.";
        passwordError2.style.display = "block";
        passwordInput2.classList.add('is-invalid');
    } else if (password2.length < 8) {
        passwordError2.textContent = "A senha deve conter pelo menos 8 caracteres.";
        passwordError2.style.display = "block";
        passwordInput2.classList.add('is-invalid');
    } else if (!hasUpperCase || !hasLowerCase || !hasNumber) {
        passwordError2.textContent = "A senha deve conter letra maiúscula, minúscula e um número.";
        passwordError2.style.display = "block";
        passwordInput2.classList.add('is-invalid');
    } else if (!hasSpecialChar) {
        passwordError2.textContent = "A senha deve conter pelo menos um caractere especial.";
        passwordError2.style.display = "block";
        passwordInput2.classList.add('is-invalid');
    } else {
        passwordError2.style.display = "none";
        passwordInput2.classList.remove('is-invalid');
        passwordInput2.classList.add('is-valid');
    }
}

// Validar Confirmar senha
function validateConfirmPassword2() {
    if (confirmPasswordInput2.value.trim() === "") {
        confirmPasswordError2.textContent = "Preencha este campo.";
        confirmPasswordError2.style.display = "block";
        confirmPasswordInput2.classList.add('is-invalid');
    } else if (confirmPasswordInput2.value !== passwordInput2.value) {
        confirmPasswordError2.textContent = "As senhas não coincidem.";
        confirmPasswordError2.style.display = "block";
        confirmPasswordInput2.classList.add('is-invalid');
    }else if (passwordInput2.classList.contains('is-invalid')) {
        confirmPasswordError2.textContent = "A senha deve ser válida primeiro.";
        confirmPasswordError2.style.display = "block";
        confirmPasswordInput2.classList.add('is-invalid');
    } else {
        confirmPasswordError2.style.display = "none";
        confirmPasswordInput2.classList.remove('is-invalid');
        confirmPasswordInput2.classList.add('is-valid');
    }
}

// Desabilitar ou habilitar o campo Inscrição Estadual com base no checkbox
function toggleInscricaoEstadual() {
    inscricaoEstadualInput.disabled = checkboxInscricaoEstadual.checked;
    if (checkboxInscricaoEstadual.checked) {
        inscricaoEstadualInput.value = ""; // Limpa o valor se desabilitar
        inscricaoEstadualError.style.display = "none"; // Oculta o erro
        inscricaoEstadualInput.classList.remove('is-invalid', 'is-valid'); // Remove classes
    }
}

// Adiciona o evento de mudança ao checkbox
checkboxInscricaoEstadual.addEventListener('change', toggleInscricaoEstadual);

// Chamada inicial para verificar se o checkbox já está selecionado
toggleInscricaoEstadual();



// Validar Checkbox
function validateCheckbox2() {
    var checkboxInput2 = document.getElementById('termsCheckbox2');
    var checkboxError2 = document.getElementById('checkboxError2');
    if (!checkboxInput2.checked) {
        checkboxError2.style.display = "block";
        checkboxError2.classList.add('d-block');
        checkboxError2.textContent = "Você deve concordar com os termos para continuar.";
    } else {
        checkboxError2.style.display = "none";
        checkboxError2.classList.remove('d-block');
        checkboxError2.textContent = "";
    }
}

// Função para alternar a visibilidade da senha
function togglePasswordVisibility2(inputId, icon) {
    const input = document.getElementById(inputId);
    const isPassword = input.type === "password";
    input.type = isPassword ? "text" : "password";
    icon.classList.toggle("bi-eye-slash-fill", isPassword);
    icon.classList.toggle("bi-eye-fill", !isPassword);
}

document.getElementById('togglePassword2').addEventListener('click', function () {
    togglePasswordVisibility2('password2', this.firstElementChild);
});

document.getElementById('toggleConfirmPassword2').addEventListener('click', function () {
    togglePasswordVisibility2('confirmPassword2', this.firstElementChild);
});

// Eventos listeners
fantasyNameInput.addEventListener('input', validateFantasyName);
razaosocialInput.addEventListener('input', validateRazaoSocial);
inscricaoEstadualInput.addEventListener('input', validateInscricaoEstadual);
cnpjInput.addEventListener('input', () => {
    formatCNPJ();
    validateCNPJ();
});
emailInput2.addEventListener('input', () => {
    validateEmail2();
    //validateAPIEmail2(); chama o API
});
phoneInput2.addEventListener('input', () => {
    formatPhone2();
    validatePhone2();
});
passwordInput2.addEventListener('input', validatePassword2);
confirmPasswordInput2.addEventListener('input', validateConfirmPassword2);
checkboxInput2.addEventListener('change', validateCheckbox2);

// Submit
registrationFormJuridica.addEventListener('submit', function(event) {
    event.preventDefault();

    validateFantasyName();
    validateRazaoSocial();
    validateInscricaoEstadual();
    validateCNPJ();
    validateEmail2();
    //validateAPIEmail2();  chama o API
    validatePhone2();
    validatePassword2();
    validateConfirmPassword2();
    validateCheckbox2();

    const isFantasyNameValid = !fantasyNameInput.classList.contains('is-invalid');
    const isRazaoSocialValid = !razaosocialInput.classList.contains('is-invalid');
    const isInscricaoEstadualValid = !inscricaoEstadualInput.classList.contains('is-invalid');
    const isCNPJValid = !cnpjInput.classList.contains('is-invalid');
    const isEmailValid2 = !emailInput2.classList.contains('is-invalid');
    const isPhoneValid2 = !phoneInput2.classList.contains('is-invalid');
    const isPasswordValid2 = !passwordInput2.classList.contains('is-invalid');
    const isConfirmPasswordValid2 = !confirmPasswordInput2.classList.contains('is-invalid');
    const isCheckboxValid2 = checkboxInput2.checked;

    if (isFantasyNameValid && isRazaoSocialValid && isInscricaoEstadualValid && isCNPJValid && isEmailValid2 && isPhoneValid2 && isPasswordValid2 && isConfirmPasswordValid2 && isCheckboxValid2) {
        alert("Cadastro realizado com sucesso!");
    }
});


// Evento listener para resetar
registrationFormJuridica.addEventListener('reset', () => {
    fantasyNameError.style.display = "none";
    razaosocialError.style.display = "none";
    inscricaoEstadualError.style.display = "none";
    cnpjError.style.display = "none";
    emailError2.style.display = 'none';
    phoneError2.style.display = "none";
    passwordError2.style.display = 'none';
    confirmPasswordError2.style.display = "none";
    fantasyNameInput.classList.remove('is-invalid', 'is-valid');
    razaosocialInput.classList.remove('is-invalid', 'is-valid');
    inscricaoEstadualInput.classList.remove('is-invalid', 'is-valid');
    cnpjInput.classList.remove('is-invalid', 'is-valid');
    emailInput2.classList.remove('is-invalid', 'is-valid');
    phoneInput2.classList.remove('is-invalid', 'is-valid');
    passwordInput2.classList.remove('is-invalid', 'is-valid');
    confirmPasswordInput2.classList.remove('is-invalid', 'is-valid');
});