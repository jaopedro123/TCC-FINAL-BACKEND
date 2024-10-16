$(document).ready(function () {
    const form = $('#CategoriaForm');

    // Inicialização de eventos
    initializeEventHandlers();

    // Adicionar eventos de input e change para validar enquanto o usuário digita
    form.find('input, select').on('input change', function () {
        validateInput($(this));
    });

    // Evento de submissão do formulário
    form.on('submit', function (event) {
        event.preventDefault();
        event.stopPropagation();

        form.removeClass('was-validated');
        if (!form[0].checkValidity()) {
            form.addClass('was-validated');
            return;
        }

        // Validação personalizada para 'nome' e 'alias'
        const isNomeValido = validateInput($('#nome'));
        const isAliasValido = validateInput($('#alias'));

        if (!isNomeValido || !isAliasValido) {
            return;
        }

        // Checar unicidade dos campos nome e alias
        checkUniqueCategoria().then((isUnique) => {
            if (isUnique) {
                form.addClass('was-validated');
                form.off('submit').submit(); // Submeter o formulário
            }
        });
    });

    // Verifica tamanho da imagem ao selecionar um arquivo
    $('#foto').on('change', function () {
        validateImageSize(this, 1);
    });

});


// Função para validar um campo de entrada
function validateInput($input) {
    const name = $input.attr('name');
    const value = $input.val();

    const regex = /^[A-Za-z0-9.\-+=&()_\/]+( [A-Za-z0-9.\-+=&()_\/]+)*$/;

    if (name === 'nome' || name === 'alias') {
        const trimmedValue = value.trim();

        if (trimmedValue === '') {
            setInvalid($input, 'Preencha este campo.');
            return false;
        }

        if (trimmedValue.length < 3) {
            setInvalid($input, 'O campo deve ter pelo menos 3 caracteres.');
            return false;
        }

        if (!regex.test(trimmedValue) || /\s{2,}/.test(trimmedValue) || value !== trimmedValue) {
            setInvalid($input, 'O campo está inválido');
            return false;
        }

        // Caso tudo esteja correto, marca como válido
        setValid($input);
        return true;
    }

    // Validação padrão para outros campos
    if ($input[0].checkValidity()) {
        setValid($input);
        return true;
    } else {
        setInvalid($input, 'Preencha este campo corretamente.');
        return false;
    }
}


// Função para marcar um campo como inválido
function setInvalid($input, message) {
    $input.removeClass('is-valid').addClass('is-invalid');
    $input.next('.invalid-feedback').text(message);
}

// Função para marcar um campo como válido
function setValid($input) {
    $input.removeClass('is-invalid').addClass('is-valid');
    $input.next('.invalid-feedback').text('');
}

// Função para validar o tamanho da imagem
function validateImageSize(inputElement, maxSizeMB) {
    const file = inputElement.files[0];

    if (!file) {
        return;
}

    const maxSize = maxSizeMB * 1024 * 1024; // Converter MB para bytes

    if (file.size > maxSize) {
        showModalDialog("Erro", `Você só pode escolher imagens abaixo de ${maxSizeMB} MB!`);
        $(inputElement).addClass('is-invalid');
        $(inputElement).next('.invalid-feedback').text(`A imagem deve ter no máximo ${maxSizeMB} MB.`);
    } else {
        $(inputElement).removeClass('is-invalid');
        $(inputElement).addClass('is-valid');
        $(inputElement).next('.invalid-feedback').text('');
        showImageThumbnail(inputElement);
    }
}

// Função para exibir o modal com mensagens de erro
function showModalDialog(title, message) {
    $('#modalTitle').text(title);
    $('#modalBody').text(message);
    $('#modalDialog').modal('show');
}

// Função para exibir a miniatura da imagem
function showImageThumbnail(fileInput) {
    const file = fileInput.files[0];
    const reader = new FileReader();

    reader.onload = function (e) {
        $('#thumbnail').attr('src', e.target.result);
    };

    reader.readAsDataURL(file);
}

// Função para verificar a unicidade de 'nome' e 'alias' via AJAX
function checkUniqueCategoria() {
    return new Promise(function (resolve, reject) {
        const categId = $('#id').val();
        const categNome = $('#nome').val().trim();
        const categAlias = $('#alias').val().trim();
        const csrfValue = $("input[name='_csrf']").val(); 

        $.ajax({
            url: "/MotherBoardAdmin/categorias/check_unique",
            type: "POST",
            data: {
                id: categId,
                nome: categNome,
                alias: categAlias,
                _csrf: csrfValue
            },
            success: function (response) {
                if (response === "OK") {
                    resolve(true);
                } else {
                    // Manipula erro de Nome Duplicado
                    if (response === "Nome Duplicado") {
                        const $nomeInput = $('#nome');
                        setInvalid($nomeInput, 'Nome já utilizado, por favor troque para prosseguir.');
                    }

                    // Manipula erro de Alias Duplicado
                    if (response === "Alias Duplicado") {
                        const $aliasInput = $('#alias');
                        setInvalid($aliasInput, 'Alias já utilizado, por favor troque para prosseguir.');
                    }

                    // Mostrar modal com erro
                    showModalDialog("Erro ao criar/atualizar categoria", "Verifique o campo destacado.");
                    resolve(false);
                }
            },
            error: function () {
                showModalDialog("Erro", "Resposta desconhecida do servidor.");
                reject(false);
            }
        });
    });
}


// Função para inicializar eventos
function initializeEventHandlers() {
    $("#buttonCancel").on("click", handleCancelButtonClick);
    $("#foto").change(function () {
        validateImageSize(this, 1, showModalDialog, showImageThumbnail);});
    $('#thumbnail').on('click', function () {
        $('#foto').click();});
}

// Evento no botão de cancelar para redirecionar
function handleCancelButtonClick() {
    window.location = "/MotherBoardAdmin/categorias";
}