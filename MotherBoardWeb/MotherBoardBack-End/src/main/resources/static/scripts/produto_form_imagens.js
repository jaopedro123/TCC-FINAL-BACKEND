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
    <div class="col-md-6 col-sm-12 mt-2 p-2" id="divImagemExtra${index}">
    <!-- Cabeçalho da Imagem Extra -->
    <div id="imagemExtraHeader${index}">
        <label>Imagem Extra #${index + 1}:</label>
    </div>

    <!-- Contêiner da imagem e input -->
    <div class="file-input-container">
        <!-- Imagem Preview (clicável) -->
        <div class="my-1">
            <img src="${defaultImageThumbnailSrc}" id="extraThumbnail${index}" alt="Preview da imagem extra"
                 class="img-fluid rounded-3 clickable-image" style="width: 150px; height: 150px; cursor: pointer;"
                 onclick="document.getElementById('fileInput${index}').click();" />
        </div>

        <!-- Campo de input para upload da imagem extra -->
        <input type="file" name="imagemExtra" class="form-control" id="fileInput${index}"
               onchange="showExtraImageThumbnail(this, ${index})"
               accept="image/png, image/jpeg" />
    </div>

    <!-- Campo oculto para ID da imagem -->
    <input type="hidden" th:field="*{imagem_principal}" />
</div>
    `;

    htmlLinkRemove = `
        <button type="button" class="btn btn-danger d-flex align-items-center" 
        onclick="removerImagemExtra(${index - 1})" title="Remover essa imagem" style="position: absolute;margin-left: 160px;margin-top: 5px;"><i class="bi bi-trash3-fill fs-4"></i></button>
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

// Adiciona um event listener para cada imagem com a classe 'clickable-image'
document.querySelectorAll('.clickable-image').forEach((img) => {
    img.addEventListener('click', function () {
        // Procura o próximo input 'file' associado à imagem clicada e ativa o clique
        const fileInput = this.parentElement.nextElementSibling;
        if (fileInput && fileInput.type === 'file') {
            fileInput.click();
        }
    });
});