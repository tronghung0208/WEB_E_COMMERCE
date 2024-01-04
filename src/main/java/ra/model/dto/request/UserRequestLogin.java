package ra.model.dto.request;

import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserRequestLogin {
    @NotBlank(message = "Email không được để trống")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",message = "email không đúng định dạng")
    private String email;
    @NotBlank(message = "Mật khẩu không được để trống")
    @Pattern(regexp = "^.{5,20}$",message = "Mật khẩu phải từ 5-20 kí tự")
    private String password;
}
