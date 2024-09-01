 // Função para validar o Nome
 function validateName(inputId, errorId) {
    const nameInput = document.getElementById(inputId);
    const nameError = document.getElementById(errorId);
    const nameRegex = /^[a-zA-Z\s]+$/; 

    if (nameInput.value.trim() === "") {
        nameError.textContent = "Preencha este campo.";
        nameError.style.display = "block";
        nameInput.classList.add('is-invalid');
    } 
    else if (nameInput.value.trim().length < 3) {
        nameError.textContent = "O nome deve conter pelo menos 3 caracteres.";
        nameError.style.display = "block";
        nameInput.classList.add('is-invalid');
    }
    else if (!nameRegex.test(nameInput.value)) {
        nameError.textContent = "O nome deve conter apenas letras.";
        nameError.style.display = "block";
        nameInput.classList.add('is-invalid');
    } else {
        nameError.style.display = "none";
        nameInput.classList.remove('is-invalid');
        nameInput.classList.add('is-valid');
    }
}

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

// Função para validar o Select
function validateSelect(selectId, errorId) {
    const selectInput = document.getElementById(selectId);
    const selectError = document.getElementById(errorId);

    if (selectInput.value === "") {
        selectError.textContent = "Selecione uma opção.";
        selectError.style.display = "block";
        selectInput.classList.add('is-invalid');
    } else {
        selectError.style.display = "none";
        selectInput.classList.remove('is-invalid');
        selectInput.classList.add('is-valid');
    }
}

// Função para validar a Descrição
function validateDescription(inputId, errorId) {
    const descriptionInput = document.getElementById(inputId);
    const descriptionError = document.getElementById(errorId);

    if (descriptionInput.value.trim() === "") {
        descriptionError.textContent = "Preencha este campo.";
        descriptionError.style.display = "block";
        descriptionInput.classList.add('is-invalid');
    } else {
        descriptionError.style.display = "none";
        descriptionInput.classList.remove('is-invalid');
        descriptionInput.classList.add('is-valid');
    }
}

// Adicionar eventos de validação
document.addEventListener('DOMContentLoaded', () => {
    const nameInput = document.getElementById('name');
    const emailInput = document.getElementById('email');
    const issueSelect = document.getElementById('issueSelect');
    const descriptionInput = document.getElementById('description');
    const form = document.getElementById('supportForm');

    nameInput.addEventListener('input', () => validateName('name', 'nameError'));
    emailInput.addEventListener('input', () => validateEmail('email', 'emailError'));
    issueSelect.addEventListener('change', () => validateSelect('issueSelect', 'issueError'));
    descriptionInput.addEventListener('input', () => validateDescription('description', 'descriptionError'));

    form.addEventListener('submit', function (event) {
        event.preventDefault();
        validateName('name', 'nameError');
        validateEmail('email', 'emailError');
        validateSelect('issueSelect', 'issueError');
        validateDescription('description', 'descriptionError');

        const isNameValid = !nameInput.classList.contains('is-invalid');
        const isEmailValid = !emailInput.classList.contains('is-invalid');
        const isSelectValid = !issueSelect.classList.contains('is-invalid');
        const isDescriptionValid = !descriptionInput.classList.contains('is-invalid');

        if (isNameValid && isEmailValid && isSelectValid && isDescriptionValid) {
            alert("Sua solicitação foi enviada com sucesso!");
            // Você pode redirecionar ou fazer uma chamada AJAX aqui
        }
    });
});