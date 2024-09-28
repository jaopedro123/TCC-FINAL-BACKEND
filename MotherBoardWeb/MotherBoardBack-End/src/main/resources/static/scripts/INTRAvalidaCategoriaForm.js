$(document).ready(function () {
    $("#buttonCancel").on("click", function () {
        window.location = "/MotherBoardAdmin/categorias";
    });

    $("#foto").change(function () {

        foto = this.files[0].size;

        // 1 MB = 1048576
        if (foto > 1048576) {
            this.setCustomValidity("Voce so pode escolher imagens abaixo de 1 MB! ");
            this.reportvalidity();
        }
        else {
            this.setCustomValidity("");
            showImageThumbnail(this);
        }
    });

});

function showImageThumbnail(fileInput) {
    var file = fileInput.files[0];
    var reader = new FileReader();
    reader.onload = function (e) {
        $("#thumbnail").attr("src", e.target.result);
    };
    reader.readAsDataURL(file);
}

function validateForm(form) {
    var checks = [checkUniqueCategoria(form)];

    Promise.all(checks).then(function (results) {
        if (results.every(result => result === true)) {
            form.submit();
        } else {
            return false;
        }
    });

    return false;
}

function checkUniqueCategoria(form) {
    return new Promise(function (resolve, reject) {
        let categId = $("#id").val();
        let categNome = $("#nome").val();
        let categAlias = $("#alias").val();
        let csrfValue = $("input[name='_csrf']").val();

        let url = "/MotherBoardAdmin/categorias/check_unique";

        let params = { id: categId, nome: categNome, alias: categAlias, _csrf: csrfValue };

        console.log(url + params)

        $.post(url, params, function (response) {

            if (response == "OK") {
                resolve(true);
            } else if (response == "Nome Duplicado") {
                showModalDialog("Erro ao criar categoria", "Nome já utilizado, por favor troque ele para prosseguir");
                resolve(false);
            } else if (response == "Alias Duplicado") {
                showModalDialog("Erro ao criar categoria", "Alias já utilizado, por favor troque ele para prosseguir");
                resolve(false);
            }

        }).fail(function () {
            showModalDialog("Erro", "Resposta desconhecida do servidor");
            reject(false); 
        });
    });
}

function showModalDialog(title, message) {
    $("#modalTitle").text(title);
    $("#modalBody").text(message);
    $("#modalDialog").modal('show');
}
document.getElementById('thumbnail').addEventListener('click', function () {
    document.getElementById('foto').click(); // Simula o clique no input de arquivo
});