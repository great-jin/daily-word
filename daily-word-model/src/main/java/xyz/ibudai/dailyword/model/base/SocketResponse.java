package xyz.ibudai.dailyword.model.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.ibudai.dailyword.model.enums.socket.SocketStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SocketResponse {

    private SocketStatus status;

    private Object data;

    public SocketResponse(SocketStatus status) {
        this.status = status;
    }
}
