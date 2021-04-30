package chess.web;

import chess.dto.RoomRequestDto;
import chess.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/rooms")
@Controller
public class RoomController {

    private final RoomService service;

    @PostMapping
    public void add(@RequestBody RoomRequestDto dto) {
        service.add(dto.getName());
    }

    @PostMapping("/{id}")
    public void delete(@PathVariable int id) {
        service.delete(id);
    }
}
