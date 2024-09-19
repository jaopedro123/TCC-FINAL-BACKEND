$(document).ready(function () {
    $("#buttonCancel").on("click", function () {
        window.location = "/MotherBoardAdmin/categorias";
    });

    $("#foto").change(function () {

        foto = this.files[0].size;

        if (foto > 102400) {
            this.setCustomValidity("Você so pode escolher imagens abaixo de 100KB! ");
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

function checkUnique(form) {
    categId = $("#id").val();
    categNome = $("#nome").val();
    categAlias = $("#alias").val();

    csrfValue = $("input[name='_csrf']").val();

    url = "/categorias/check_unique";

    params = { id: categId, nome: categNome, alias: categAlias, _csrf: csrfValue };

    $.post(url, params, function (response) {

        if (response == "OK") {
            return true;
        } else if (response == "Nome Duplicado") {
            showModalDialog("Erro ao criar categoria", "Nome já utilizado, por favor troque ele para prosseguir");
            return false;
        } else if (response == "Alias Duplicado") {
            showModalDialog("Erro ao criar categoria", "Alias já utilizado, por favor troque ele para prosseguir");
        }

    }).fail(function () {
        showModalDialog("Erro", "Resposta desconhecida do servidor");
        return false;
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