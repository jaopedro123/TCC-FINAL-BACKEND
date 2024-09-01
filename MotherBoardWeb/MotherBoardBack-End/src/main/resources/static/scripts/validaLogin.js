
const emailInput = document.getElementById('email');
const passwordInput = document.getElementById('password');
const emailError = document.getElementById('emailError');
const passwordError = document.getElementById('passwordError');
const loginForm = document.getElementById('loginForm');

function validateEmail() {
    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (emailInput.value === "") {
        emailError.textContent = "Preencha este campo.";
        emailError.style.display = "block";
        emailInput.classList.add('is-invalid');
        emailInput.classList.remove('is-valid');
    } else if (!emailPattern.test(emailInput.value)) {
        emailError.textContent = "Por favor, insira um email válido.";
        emailError.style.display = "block";
        emailInput.classList.add('is-invalid');
        emailInput.classList.remove('is-valid');
    } else {
        emailError.style.display = "none";
        emailInput.classList.remove('is-invalid');
        emailInput.classList.add('is-valid');
    }
}

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

emailInput.addEventListener('input', validateEmail);
passwordInput.addEventListener('input', validatePassword);

loginForm.addEventListener('submit', function(event) {
    validateEmail();
    validatePassword();

    const isEmailValid = !emailInput.classList.contains('is-invalid');
    const isPasswordValid = !passwordInput.classList.contains('is-invalid');

    if (!isEmailValid || !isPasswordValid) {
        event.preventDefault();
    }
});
