$(document).ready(function () {
    // Evento no botão de cancelar para redirecionar
    $("#buttonCancel").on("click", function () {
        window.location = "/MotherBoardAdmin/categorias";
    });

    $("#foto").change(function () {
        validateImageSize(this, 1, showModalDialog, showImageThumbnail);
    });
    
    // Pré-visualização de imagem selecionada
    document.getElementById('thumbnail').addEventListener('click', function () {
        document.getElementById('foto').click(); // Simula o clique no input de arquivo
    });

    // Validar o formulário usando Bootstrap e `checkUniqueCategoria`
    const forms = document.querySelectorAll('.needs-validation');
    Array.from(forms).forEach(function (form) {
        form.addEventListener('submit', function (event) {
            event.preventDefault();
            event.stopPropagation();

            if (!form.checkValidity()) {
                form.classList.add('was-validated');
                return; 
            }

            checkUniqueCategoria(form).then((isUnique) => {
                if (isUnique) {
                    form.classList.add('was-validated');
                    form.submit(); 
                }
            });   
        }, false);
    });
});

function validateImageSize(inputElement, maxSizeMB, showModalDialog, showImageThumbnail) {
    const file = inputElement.files[0];
    
    if (!file) {
        return;
    }

    const maxSize = maxSizeMB * 1024 * 1024; 

    if (file.size > maxSize) {
        showModalDialog("Desculpe...", `Você só pode escolher imagens abaixo de ${maxSizeMB} MB!`);
        inputElement.setCustomValidity("Tamanho de imagem inválido"); 
    } else {
        inputElement.setCustomValidity("");
        showImageThumbnail(inputElement); 
    }
} 

// Função de verificação única para nome e alias da categoria
function checkUniqueCategoria(form) {
    return new Promise(function (resolve, reject) {
        let categId = $("#id").val();
        let categNome = $("#nome").val();
        let categAlias = $("#alias").val();
        let csrfValue = $("input[name='_csrf']").val();

        let url = "/MotherBoardAdmin/categorias/check_unique";

        let params = { id: categId, nome: categNome, alias: categAlias, _csrf: csrfValue };

        $.post(url, params, function (response) {
            if (response === "OK") {
                resolve(true); // Verificação única bem-sucedida
            } else if (response === "Nome Duplicado") {
                showModalDialog("Erro ao criar categoria", "Nome já utilizado, por favor troque para prosseguir");
                resolve(false);
            } else if (response === "Alias Duplicado") {
                showModalDialog("Erro ao criar categoria", "Alias já utilizado, por favor troque para prosseguir");
                resolve(false);
            }
        }).fail(function () {
            showModalDialog("Erro", "Resposta desconhecida do servidor");
            reject(false);
        });
    });
}

// Função para exibir o modal com mensagens de erro
function showModalDialog(title, message) {
    $("#modalTitle").text(title);
    $("#modalBody").text(message);
    $("#modalDialog").modal('show');
}

// Função para exibir a miniatura da imagem
function showImageThumbnail(fileInput) {
    var file = fileInput.files[0];
    var reader = new FileReader();
    reader.onload = function (e) {
        $("#thumbnail").attr("src", e.target.result);
    };
    reader.readAsDataURL(file);
}

// Impedir caracteres especiais no campo de nome da categoria
$("#nome").on("input", function () {
    this.value = this.value.replace(/[^a-zA-ZÀ-ÿ\s]/g, '');
});