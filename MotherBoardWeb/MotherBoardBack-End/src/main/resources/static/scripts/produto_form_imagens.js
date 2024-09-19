var extraImagesCount = 0;

$(document).ready(function () {

    $("#fileImage").change(function () {
        if (!checkFileSize(this)) {
            return;
        }
        showImageThumbnail(this);
    });

    $("input[name='imagemExtra']").each(function(index) {
        extraImagesCount++;

        $(this).change(function() {
            showExtraImageThumbnail(this, index);
        })
    });

    $("a[name='linkRemoverImagemExtra']").each(function(index) {
        $(this).click(function() {
            removerImagemExtra(index);
        })
    })
});

function showImageThumbnail(fileInput) {
    var file = fileInput.files[0];
    var reader = new FileReader();
    reader.onload = function (e) {
        $("#thumbnail").attr("src", e.target.result);
    };
    reader.readAsDataURL(file);
}

function showExtraImageThumbnail(fileInput, index) {
    var file = fileInput.files[0];

    fileName = file.name;
    imagemNomeHiddenField = $("#imagemNome" + index);
    if (imagemNomeHiddenField.length) {
        imagemNomeHiddenField.val(fileName);
    }

    var reader = new FileReader();
    reader.onload = function (e) {
        $("#extraThumbnail" + index).attr("src", e.target.result);
    };
    reader.readAsDataURL(file);

    if(index >= extraImagesCount - 1) {
        addNextExtraImageSection(index + 1);
    }
}

function removerImagemExtra(index) {
    $("#divImagemExtra" + index).remove();
}

function addNextExtraImageSection(index) {
    htmlImagemExtra = `
            <div class="col-5 mt-2 p-2" id="divImagemExtra${index}">
                <div id="imagemExtraHeader${index}"><label>Imagem Extra #${index + 1}:</label></div>
                <div class="col-sm-8">
                    <input type="hidden" th:field="*{imagem_principal}" />
                    <div class="my-1">
                        <img src="${defaultImageThumbnailSrc}" id="extraThumbnail${index}"
                        style="width: 130px; height: 130px;">
                    </div>
                    <input type="file" name="imagemExtra" 
                        onchange="showExtraImageThumbnail(this, ${index})"
                        accept="image/png, image/jpeg" />
                </div>
            </div>
    `;

    htmlLinkRemove = `
            <a href="javascript:removerImagemExtra(${index - 1})" title="Remover essa imagem">Remover</a>
    `;

    $("#divImagensProduto").append(htmlImagemExtra);

    $("#imagemExtraHeader" + (index - 1)).append(htmlLinkRemove);

    extraImagesCount++;

}

function checkFileSize(fileInput) {
    fileSize = fileInput.files[0].size;

    if (fileSize > 502400) {
        fileInput.setCustomValidity("Você so pode escolher imagens abaixo de 500KB! ");
        fileInput.reportvalidity();

        return false;
    }
    else {
        fileInput.setCustomValidity("");

        return true;
    }
}

function showModalDialog(title, message) {
    $("#modalTitle").text(title);
    $("#modalBody").text(message);
    $("#modalDialog").modal('show');
}