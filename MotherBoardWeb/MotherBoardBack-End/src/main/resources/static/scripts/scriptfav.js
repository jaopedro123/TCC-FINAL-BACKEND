function showDescription(button) {
      var cardBody = button.parentElement.parentElement;
      var description = cardBody.querySelector('.card-textsexo');
      description.style.display = description.style.display === 'block' ? 'none' : 'block';
    }