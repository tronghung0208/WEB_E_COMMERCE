package ra.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UpdatePasswordRequest {
    @NotBlank(message = "currentPassword không được để trống")
    @Pattern(regexp = "^.{5,20}$",message = "Mật khẩu phải từ 5-20 kí tự")
    private String currentPassword;
    @NotBlank(message = "newPassword không được để trống")
    @Pattern(regexp = "^.{5,20}$",message = "Mật khẩu phải từ 5-20 kí tự")
    private String newPassword;
    @NotBlank(message = "confirmPassword không được để trống")
    @Pattern(regexp = "^.{5,20}$",message = "Mật khẩu phải từ 5-20 kí tự")
    private String confirmPassword;
}
