// Função para detectar a bandeira do cartão
function detectCardBrand(cardNumber) {
    const cleanedNumber = cardNumber.replace(/\s+/g, ''); // Remove espaços
    const visaPattern = /^4[0-9]{15}(?:[0-9]{3})?$/;
    const mastercardPattern = /^5[1-5][0-9]{14}$/;

    if (visaPattern.test(cleanedNumber)) {
        return 'visa';
    } else if (mastercardPattern.test(cleanedNumber)) {
        return 'mastercard';
    } else {
        return 'unknown';
    }
}

// Atualizar a bandeira do cartão ao digitar o número do cartão
document.getElementById('cardNumber').addEventListener('input', function () {
    let cardNumber = this.value.replace(/\D/g, ''); // Remove todos os caracteres não numéricos

    // Adiciona espaços a cada 4 dígitos
    cardNumber = cardNumber.replace(/(.{4})/g, '$1 ').trim();

    this.value = cardNumber; // Atualiza o campo com a formatação
    
    document.getElementById('displayCardNumber').textContent = cardNumber || '0000 0000 0000 0000';
    
    const cardBrand = detectCardBrand(cardNumber.replace(/\s+/g, '')); // Remove espaços para detecção

    const logoElement = document.querySelector('.logocartaobandeira img');

    if (cardBrand === 'visa') {
        logoElement.src = 'icones/Visa-Logo.png';
        logoElement.alt = 'Visa';
    } else if (cardBrand === 'mastercard') {
        logoElement.src = 'Imagens/Mastercard-Logo.png';
        logoElement.alt = 'Mastercard';
    } else {
        logoElement.src = 'icones/inv.png'; // Se quiser deixar sem imagem quando for "unknown"
        logoElement.alt = 'Unknown';
    }
});

document.getElementById('cvv').addEventListener('focus', function () {
    document.querySelector('.credit-card').classList.add('flipped');
});

document.getElementById('cvv').addEventListener('blur', function () {
    document.querySelector('.credit-card').classList.remove('flipped');
});

document.getElementById('cvv').addEventListener('input', function () {
    const cvvValue = this.value.replace(/[^0-9]/g, '');
    document.getElementById('displayCVV').textContent = cvvValue || 'XXX';
});

document.addEventListener('DOMContentLoaded', function() {
    const pixForm = document.getElementById('pixForm');
    const boletoForm = document.getElementById('boletoForm');
    const creditoForm = document.getElementById('creditoForm');

    const fullNamePix = document.getElementById('fullNamePix');
    const cpfPix = document.getElementById('cpfPix');
    const submitPixButton = document.getElementById('submitPixButton');
    const modalPix = new bootstrap.Modal(document.getElementById('staticBackdrop3'));

    const fullNameBoleto = document.getElementById('fullNameBoleto');
    const cpfBoleto = document.getElementById('cpfBoleto');
    const submitBoletoButton = document.getElementById('submitBoletoButton');
    const modalBoleto = new bootstrap.Modal(document.getElementById('staticBackdrop2'));

    const cardNumber = document.getElementById('cardNumber');
    const cardName = document.getElementById('cardName');
    const cpf = document.getElementById('cpf');
    const expirationDate = document.getElementById('expirationDate');
    const cvv = document.getElementById('cvv');
    const parcelas = document.getElementById('validationDefault04');
    const creditoSubmitButton = document.querySelector('#creditoForm .btn.botaopagamento');
    const creditoModal = new bootstrap.Modal(document.getElementById('staticBackdrop'));

    function validateName(name) {
        // Remove espaços extras e verifica o comprimento mínimo e se contém apenas letras e espaços
        const cleanedName = name.trim();
        return cleanedName.length >= 3 && /^[A-Za-z\s]+$/.test(cleanedName);
    }

    function validateCPF(cpf) {
        cpf = cpf.replace(/[^\d]+/g, '');
        if (cpf.length !== 11 || /^(\d)\1+$/.test(cpf)) return false;

        const calcDigit = (cpf, factor) => {
            let sum = 0;
            for (let i = 0; i < factor - 1; i++) {
                sum += cpf[i] * (factor - i);
            }
            let result = (sum * 10) % 11;
            return result === 10 ? 0 : result;
        };

        const firstDigit = calcDigit(cpf, 10);
        const secondDigit = calcDigit(cpf, 11);

        return cpf[9] == firstDigit && cpf[10] == secondDigit;
    }

    function validateCardNumber(number) {
        const cleanedNumber = number.replace(/\s+/g, '');
        return /^\d{16}$/.test(cleanedNumber);
    }

    function validateCardName(name) {
        // Use the same validation as for fullName
        return validateName(name);
    }

    function validateExpirationDate(date) {
        const dateRegex = /^(0[1-9]|1[0-2])\/\d{2}$/;
        return dateRegex.test(date);
    }

    function validateCVV(cvv) {
        const cvvRegex = /^\d{3}$/;
        return cvvRegex.test(cvv);
    }

    function validateParcelas(parcelasValue) {
        const validOptions = [
            '12x (sem juros)', '11x (sem juros)', '10x (sem juros)',
            '9x (sem juros)', '8x (sem juros)', '7x (sem juros)',
            '6x (sem juros)', '5x (sem juros)', '4x (sem juros)',
            '3x (sem juros)', '2x (sem juros)', '1x (sem juros)'
        ];
        return validOptions.includes(parcelasValue);
    }

    function updateFieldStatus(field, isValid) {
        if (isValid) {
            field.classList.remove('is-invalid');
            field.classList.add('is-valid');
        } else {
            field.classList.remove('is-valid');
            field.classList.add('is-invalid');
        }

        const errorElement = document.getElementById(`${field.id}Error`);
        if (errorElement) {
            errorElement.style.display = isValid || field.value.trim() === '' ? 'none' : 'block';
            errorElement.textContent = isValid || field.value.trim() === '' ? '' : getErrorMessage(field.id);
        }
    }

    function validateField(field) {
        let isValid;
        switch (field.id) {
            case 'fullNamePix':
            case 'fullNameBoleto':
            case 'cardName':
                isValid = validateName(field.value);
                break;
            case 'cpfPix':
            case 'cpfBoleto':
            case 'cpf':
                isValid = validateCPF(field.value);
                break;
            case 'cardNumber':
                isValid = validateCardNumber(field.value);
                break;
            case 'expirationDate':
                isValid = validateExpirationDate(field.value);
                break;
            case 'cvv':
                isValid = validateCVV(field.value);
                break;
            case 'validationDefault04':
                isValid = validateParcelas(field.value);
                break;
            default:
                isValid = true;
        }
        updateFieldStatus(field, isValid);
    }

    function getErrorMessage(fieldId) {
        switch (fieldId) {
            case 'fullNamePix':
            case 'fullNameBoleto':
            case 'cardName':
                return 'Nome inválido.';
            case 'cpfPix':
            case 'cpfBoleto':
            case 'cpf':
                return 'CPF inválido.';
            case 'cardNumber':
                return 'Número do cartão inválido.';
            case 'expirationDate':
                return 'Data de validade inválida.';
            case 'cvv':
                return 'CVV inválido.';
            case 'validationDefault04':
                return 'Parcela inválida.';
            default:
                return '';
        }
    }

    function handleInput(event) {
        validateField(event.target);
    }

    function handleInputCredito(event) {
        validateField(event.target);
        updateCardDisplay();
    }

    function handleFormSubmission(form, nameField, cpfField, modal) {
        const nameIsValid = validateName(nameField.value);
        const cpfIsValid = validateCPF(cpfField.value);

        updateFieldStatus(nameField, nameIsValid);
        updateFieldStatus(cpfField, cpfIsValid);

        if (nameIsValid && cpfIsValid) {
            modal.show();
        }
    }

    function updateCardDisplay() {
        const formattedCardNumber = cardNumber.value.replace(/\D/g, '').replace(/(.{4})/g, '$1 ').trim();
        document.getElementById('displayCardNumber').textContent = formattedCardNumber || '0000 0000 0000 0000';
        document.getElementById('displayCardHolder').textContent = cardName.value || 'Titular do cartão';
        document.getElementById('displayExpires').textContent = expirationDate.value || 'MM/AA';
        document.getElementById('displayCVV').textContent = cvv.value || '000';
    }

    // Aplicar máscara para a data de validade
    function applyExpirationDateMask(expirationDateField) {
        expirationDateField.addEventListener('input', function (e) {
            let value = e.target.value.replace(/\D/g, ''); // Remove todos os caracteres não numéricos
            if (value.length > 2) {
                value = value.substring(0, 2) + '/' + value.substring(2, 4);
            }
            e.target.value = value;
        });
    }

    // Aplicar máscara no campo de data de validade
    const expirationDateField = document.getElementById('expirationDate');
    if (expirationDateField) {
        applyExpirationDateMask(expirationDateField);
    }

    // Adicionar máscara para CPF
    function applyCPFMask(cpfField) {
        cpfField.addEventListener('input', function (e) {
            let value = e.target.value.replace(/\D/g, '');
            if (value.length > 9) value = value.substring(0, 9) + '-' + value.substring(9, 11);
            if (value.length > 6) value = value.substring(0, 6) + '.' + value.substring(6);
            if (value.length > 3) value = value.substring(0, 3) + '.' + value.substring(3);

            e.target.value = value;
        });
    }

    // Aplicar máscara nos campos de CPF
    applyCPFMask(cpfPix);
    applyCPFMask(cpfBoleto);
    applyCPFMask(cpf);

    if (fullNamePix && cpfPix && submitPixButton) {
        fullNamePix.addEventListener('input', handleInput);
        cpfPix.addEventListener('input', handleInput);
        submitPixButton.addEventListener('click', () => handleFormSubmission(pixForm, fullNamePix, cpfPix, modalPix));
    }

    if (fullNameBoleto && cpfBoleto && submitBoletoButton) {
        fullNameBoleto.addEventListener('input', handleInput);
        cpfBoleto.addEventListener('input', handleInput);
        submitBoletoButton.addEventListener('click', () => handleFormSubmission(boletoForm, fullNameBoleto, cpfBoleto, modalBoleto));
    }

    if (creditoForm && cardNumber && cardName && cpf && expirationDate && cvv && parcelas && creditoSubmitButton) {
        cardNumber.addEventListener('input', handleInputCredito);
        cardName.addEventListener('input', handleInputCredito);
        cpf.addEventListener('input', handleInputCredito);
        expirationDate.addEventListener('input', handleInputCredito);
        cvv.addEventListener('input', handleInputCredito);
        parcelas.addEventListener('change', handleInputCredito);

        creditoForm.addEventListener('submit', function(event) {
            event.preventDefault();
            handleInputCredito({ target: cardNumber });
            handleInputCredito({ target: cardName });
            handleInputCredito({ target: cpf });
            handleInputCredito({ target: expirationDate });
            handleInputCredito({ target: cvv });
            handleInputCredito({ target: parcelas });
            const isValid = validateCardNumber(cardNumber.value) &&
                            validateCardName(cardName.value) &&
                            validateCPF(cpf.value) &&
                            validateExpirationDate(expirationDate.value) &&
                            validateCVV(cvv.value) &&
                            validateParcelas(parcelas.value);
            if (isValid) {
                creditoModal.show();
            }
        });
    }
});
