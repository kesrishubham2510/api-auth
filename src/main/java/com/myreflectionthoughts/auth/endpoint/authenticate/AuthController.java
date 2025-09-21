package com.myreflectionthoughts.auth.endpoint.authenticate;

import com.myreflectionthoughts.auth.config.RestConstant;
import com.myreflectionthoughts.auth.datamodel.request.LoginModel;
import com.myreflectionthoughts.auth.datamodel.request.RegistrationModel;
import com.myreflectionthoughts.auth.datamodel.response.ErrorResponse;
import com.myreflectionthoughts.auth.datamodel.response.LoginDTO;
import com.myreflectionthoughts.auth.datamodel.response.RegistrationDTO;
import com.myreflectionthoughts.auth.dataprovider.service.UserProvider;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(RestConstant.API_PREFIX)
public class AuthController {

    private final UserProvider userProvider;

    public AuthController(UserProvider userProvider) {
        this.userProvider = userProvider;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "The user is registered successfully", headers = {@Header(name = "Content-Type", description = "The format of response", schema = @Schema(type = "string", example = "text/json")), @Header(name = "X-request-Id", description = "The UUID of the request", schema = @Schema(type = "string", example = "2323432432432"))}, content = @Content(schema = @Schema(implementation = RegistrationDTO.class))),
            @ApiResponse(responseCode = "400", description = "The user input is not acceptable, refer response for more details", headers = {@Header(name = "Content-Type", description = "The format of response", schema = @Schema(type = "string", example = "text/json")), @Header(name = "X-request-Id", description = "The UUID of the request", schema = @Schema(type = "string", example = "2323432432432"))}, content = @Content(schema = @Schema(implementation = ErrorResponse.class)))}
    )
    @PostMapping(RestConstant.REGISTER_PATH)
    public ResponseEntity<RegistrationDTO> registerUser(@RequestBody RegistrationModel registrationModel) {
        return ResponseEntity.ok().body(userProvider.registerUser(registrationModel));
    }


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The user is logged successfully", headers = {@Header(name = "Content-Type", description = "The format of response", schema = @Schema(type = "string", example = "text/json")), @Header(name = "X-request-Id", description = "The UUID of the request", schema = @Schema(type = "string", example = "2323432432432"))}, content = @Content(schema = @Schema(implementation = LoginDTO.class))),
            @ApiResponse(responseCode = "400", description = "The user input is not acceptable, refer response for more details", headers = {@Header(name = "Content-Type", description = "The format of response", schema = @Schema(type = "string", example = "text/json")), @Header(name = "X-request-Id", description = "The UUID of the request", schema = @Schema(type = "string", example = "2323432432432"))}, content = @Content(schema = @Schema(implementation = ErrorResponse.class)))}
    )
    @PostMapping(RestConstant.LOGIN_PATH)
    public ResponseEntity<LoginDTO> loginUser(@RequestBody LoginModel loginModel){
        return ResponseEntity.ok().body(null);
    }
}
