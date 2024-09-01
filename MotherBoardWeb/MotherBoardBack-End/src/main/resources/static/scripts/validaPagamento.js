
        // Função para validar Nome Completo
        function validateFullName(inputId, errorId) {
            const fullNameInput = document.getElementById(inputId);
            const fullNameError = document.getElementById(errorId);

            const regex = /^[A-Za-zÀ-ÿ\s]+$/; // Permite apenas letras e espaços (inclui acentos)

            if (fullNameInput.value.trim() === "") {
                fullNameError.textContent = "Preencha este campo.";
                fullNameError.style.display = "block";
                fullNameInput.classList.add('is-invalid');
            } else if (fullNameInput.value.trim().length < 3) {
                fullNameError.textContent = "O nome deve conter pelo menos 3 caracteres.";
                fullNameError.style.display = "block";
                fullNameInput.classList.add('is-invalid');
            } else if (!regex.test(fullNameInput.value.trim())) {
                fullNameError.textContent = "O nome deve conter apenas letras e espaços.";
                fullNameError.style.display = "block";
                fullNameInput.classList.add('is-invalid');
            } else {
                fullNameError.style.display = "none";
                fullNameInput.classList.remove('is-invalid');
                fullNameInput.classList.add('is-valid');
            }
        }

        // Função para mascara de CPF
        function formatCPF(inputId) {
            const cpfInput = document.getElementById(inputId);
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
            [9, 10].forEach(function (j) {
                let soma = 0, r;
                cpf.split(/(?=)/).splice(0, j).forEach(function (e, i) {
                    soma += parseInt(e) * ((j + 2) - (i + 1));
                });
                r = soma % 11;
                r = (r < 2) ? 0 : 11 - r;
                if (r != cpf.substring(j, j + 1)) result = false;
            });
            return result;
        }

        // Validação de CPF com a nova função
        function validateCPF(inputId, errorId) {
            const cpfInput = document.getElementById(inputId);
            const cpfError = document.getElementById(errorId);

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

        // Função para adicionar eventos de validação
        function addValidationEvents(formId, fullNameId, cpfId, fullNameErrorId, cpfErrorId) {
            const fullNameInput = document.getElementById(fullNameId);
            const cpfInput = document.getElementById(cpfId);
            const form = document.getElementById(formId);

            fullNameInput.addEventListener('input', () => validateFullName(fullNameId, fullNameErrorId));
            cpfInput.addEventListener('input', () => {
                formatCPF(cpfId);
                validateCPF(cpfId, cpfErrorId);
            });

            form.addEventListener('submit', function (event) {
                event.preventDefault();

                validateFullName(fullNameId, fullNameErrorId);
                validateCPF(cpfId, cpfErrorId);

                const isFullNameValid = !fullNameInput.classList.contains('is-invalid');
                const isCPFValid = !cpfInput.classList.contains('is-invalid');

                if (isFullNameValid && isCPFValid) {
                    alert("Forma de pagamento gerada, confira seu email para confirmar!");
                }
            });
        }
        // Função para formatar o Número do Cartão com espaços a cada 4 dígitos
function formatCardNumber(inputId) {
    const cardNumberInput = document.getElementById(inputId);
    let cardNumberValue = cardNumberInput.value.replace(/\D/g, ''); // Remove caracteres não numéricos
    
    // Adiciona espaços a cada 4 dígitos
    cardNumberValue = cardNumberValue.replace(/(\d{4})(?=\d)/g, '$1 ');

    cardNumberInput.value = cardNumberValue;
}

// Função para validar o Número do Cartão
function validateCardNumber(inputId, errorId) {
    const cardNumberInput = document.getElementById(inputId);
    const cardNumberError = document.getElementById(errorId);

    // Remove espaços para validação
    const cardNumberValue = cardNumberInput.value.replace(/\s+/g, '');
    
    // Validação para 14, 15 ou 16 dígitos
    const regex = /^(?:\d{14}|\d{15}|\d{16})$/;
    if (cardNumberValue === "") {
        cardNumberError.textContent = "Preencha este campo.";
        cardNumberError.style.display = "block";
        cardNumberInput.classList.add('is-invalid');
    } else if (!regex.test(cardNumberValue)) {
        cardNumberError.textContent = "Número do cartão deve ter 14, 15 ou 16 dígitos.";
        cardNumberError.style.display = "block";
        cardNumberInput.classList.add('is-invalid');
    } else {
        cardNumberError.style.display = "none";
        cardNumberInput.classList.remove('is-invalid');
        cardNumberInput.classList.add('is-valid');
    }
}

// Função para formatar a Validade do Cartão com '/'
function formatExpiryDate(inputId) {
    const expiryDateInput = document.getElementById(inputId);
    let expiryDateValue = expiryDateInput.value.replace(/\D/g, ''); // Remove caracteres não numéricos
    
    // Adiciona '/' após o mês (2 dígitos)
    if (expiryDateValue.length > 2) {
        expiryDateValue = expiryDateValue.slice(0, 2) + '/' + expiryDateValue.slice(2, 4);
    }
    
    expiryDateInput.value = expiryDateValue;
}

// Função para validar a Validade do Cartão
function validateExpiryDate(inputId, errorId) {
    const expiryDateInput = document.getElementById(inputId);
    const expiryDateError = document.getElementById(errorId);

    const expiryDateValue = expiryDateInput.value;
    const regex = /^(0[1-9]|1[0-2])\/\d{2}$/; // Validação básica para MM/AA
    if (expiryDateValue === "") {
        expiryDateError.textContent = "Preencha este campo.";
        expiryDateError.style.display = "block";
        expiryDateInput.classList.add('is-invalid');
    } else if (!regex.test(expiryDateValue)) {
        expiryDateError.textContent = "Data inválida.";
        expiryDateError.style.display = "block";
        expiryDateInput.classList.add('is-invalid');
    } else {
        expiryDateError.style.display = "none";
        expiryDateInput.classList.remove('is-invalid');
        expiryDateInput.classList.add('is-valid');
    }
}

// Função para validar o CVV
function validateCVV(inputId, errorId) {
    const cvvInput = document.getElementById(inputId);
    const cvvError = document.getElementById(errorId);
    
    const cvvValue = cvvInput.value;
    const regex = /^[0-9]{3,4}$/; // CVV pode ter 3 ou 4 dígitos
    if (cvvValue === "") {
        cvvError.textContent = "Preencha este campo.";
        cvvError.style.display = "block";
        cvvInput.classList.add('is-invalid');
    } else if (!regex.test(cvvValue)) {
        cvvError.textContent = "CVV inválido.";
        cvvError.style.display = "block";
        cvvInput.classList.add('is-invalid');
    } else {
        cvvError.style.display = "none";
        cvvInput.classList.remove('is-invalid');
        cvvInput.classList.add('is-valid');
    }
}

// Função para adicionar eventos de validação e formatação
function addCreditCardValidationEvents() {
    const cardNumberInput = document.getElementById('cardNumber');
    const expiryDateInput = document.getElementById('expiryDate');
    const cvvInput = document.getElementById('cvv');
    const form = document.getElementById('creditoForm');

    cardNumberInput.addEventListener('input', () => {
        formatCardNumber('cardNumber');
        validateCardNumber('cardNumber', 'cardNumberError');
    });
    expiryDateInput.addEventListener('input', () => {
        formatExpiryDate('expiryDate');
        validateExpiryDate('expiryDate', 'expiryDateError');
    });
    cvvInput.addEventListener('input', () => validateCVV('cvv', 'cvvError'));

    form.addEventListener('submit', function (event) {
        event.preventDefault();

        validateCardNumber('cardNumber', 'cardNumberError');
        validateExpiryDate('expiryDate', 'expiryDateError');
        validateCVV('cvv', 'cvvError');

        const isCardNumberValid = !cardNumberInput.classList.contains('is-invalid');
        const isExpiryDateValid = !expiryDateInput.classList.contains('is-invalid');
        const isCVVValid = !cvvInput.classList.contains('is-invalid');

        if (isCardNumberValid && isExpiryDateValid && isCVVValid) {
            alert("Pagamento confirmado!");
        }
    });
}

// Adiciona validação para o formulário de Crédito
addCreditCardValidationEvents();


        // Adiciona eventos de validação para formulários Pix e Boleto
        addValidationEvents('pixForm', 'fullNamePix', 'cpfPix', 'fullNamePixError', 'cpfPixError');
        addValidationEvents('boletoForm', 'fullNameBoleto', 'cpfBoleto', 'fullNameBoletoError', 'cpfBoletoError');
        addValidationEvents('creditoForm', 'fullNameCredito', 'cpfCredito', 'fullNameCreditoError', 'cpfCreditoError');

