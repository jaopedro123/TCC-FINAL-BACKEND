package com.MotherBoard.Admin.marca;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "marca nao encontrada")
public class MarcaNotFoundRestException extends Exception {

}
