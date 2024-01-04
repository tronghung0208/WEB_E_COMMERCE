package ra.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserRequestRegister {
    @NotBlank(message = "Tên không được để trống")
    private String fullName;
    @NotBlank(message = "Email không được để trống")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",message = "email không đúng định dạng")
    private String email;
    @NotBlank(message = "Mật khẩu không được để trống")
    @Pattern(regexp = "^.{5,20}$",message = "Mật khẩu phải từ 5-20 kí tự")
    private String password;
    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^(84|0[3|5|7|8|9])+([0-9]{8})\\b", message = "Số điện thoại không đúng định dạng(SDT Việt Nam)")
    private String phone;
    private Set<String> roles;
}
