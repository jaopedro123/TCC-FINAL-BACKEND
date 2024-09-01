// Função para validar o E-mail
function validateEmail(inputId, errorId) {
    const emailInput = document.getElementById(inputId);
    const emailError = document.getElementById(errorId);

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/; // Regex básica para validação de e-mail

    if (emailInput.value.trim() === "") {
        emailError.textContent = "Preencha este campo.";
        emailError.style.display = "block";
        emailInput.classList.add('is-invalid');
    } else if (!emailRegex.test(emailInput.value.trim())) {
        emailError.textContent = "E-mail inválido.";
        emailError.style.display = "block";
        emailInput.classList.add('is-invalid');
    } else {
        emailError.style.display = "none";
        emailInput.classList.remove('is-invalid');
        emailInput.classList.add('is-valid');
    }
}

// Adicionar eventos de validação
document.addEventListener('DOMContentLoaded', () => {
    const emailInput = document.getElementById('email');
    const form = document.getElementById('forgotPasswordForm');

    emailInput.addEventListener('input', () => validateEmail('email', 'emailError'));

    form.addEventListener('submit', function (event) {
        event.preventDefault();
        validateEmail('email', 'emailError');

        const isEmailValid = !emailInput.classList.contains('is-invalid');

        if (isEmailValid) {
            // Aqui você pode adicionar a lógica para enviar o e-mail
            const successModal = new bootstrap.Modal(document.getElementById('successModal'));
            successModal.show();
        }
    });
});