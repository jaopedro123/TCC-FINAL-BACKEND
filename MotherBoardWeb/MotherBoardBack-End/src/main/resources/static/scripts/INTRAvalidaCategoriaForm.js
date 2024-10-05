$(document).ready(function () {
    // Evento no botão de cancelar para redirecionar
    $("#buttonCancel").on("click", function () {
        window.location = "/MotherBoardAdmin/categorias";
    });

    // Verificação do tamanho da imagem selecionada
    $("#foto").change(function () {
        const foto = this.files[0].size;

        // Limite de 1 MB (1048576 bytes)
        if (foto > 1048576) {
            this.setCustomValidity("Você só pode escolher imagens abaixo de 1 MB! ");
            this.reportValidity();
        } else {
            this.setCustomValidity("");
            showImageThumbnail(this);
        }
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

            // Validação padrão do Bootstrap
            if (!form.checkValidity()) {
                form.classList.add('was-validated');
                return; // Não continua se a validação básica falhar
            }

            // Se validação do Bootstrap estiver correta, verifica `checkUniqueCategoria`
            checkUniqueCategoria(form).then((isUnique) => {
                if (isUnique) {
                    form.classList.add('was-validated');
                    form.submit(); // Submete o formulário se `checkUniqueCategoria` retornar `true`
                }
            });
        }, false);
    });
});

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