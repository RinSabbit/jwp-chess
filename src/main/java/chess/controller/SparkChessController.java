package chess.controller;

import static chess.util.JsonParser.json;
import static spark.Spark.get;
import static spark.Spark.path;
import static spark.Spark.post;

import chess.Dto.BoardDto;
import chess.Dto.MoveRequest;
import chess.Dto.PathDto;
import chess.domain.piece.PieceColor;
import chess.service.ChessService;
import com.google.gson.Gson;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.handlebars.HandlebarsTemplateEngine;

public class SparkChessController {

    private final ChessService service;

    public SparkChessController() {
        service = new ChessService();
    }

    private static String render(Map<String, String> model) {
        return new HandlebarsTemplateEngine().render(new ModelAndView(model, "index.html"));
    }

    public void run() {
        get("/", this::renderInitBoard);
        get("/board", this::getNewBoard, json());
        get("/load", this::loadBoard, json());

        path("/board", () -> {
            get("/restart", this::getNewBoard, json());
            get("/status", this::isEnd, json());
            get("/turn", this::getTurn, json());
            get("/score", this::getScore, json());

            post("/path", this::movablePath, json());
            post("/move", this::move, json());
        });
    }

    private BoardDto loadBoard(final Request request, final Response response) {
        return new BoardDto(service.findBoard());
    }

    private List<String> movablePath(final Request request, final Response response) {
        PathDto dto = new Gson().fromJson(request.body(), PathDto.class);
        return service.findPath(dto.getFrom());
    }

    private boolean move(final Request request, final Response response) {
        MoveRequest dto = new Gson().fromJson(request.body(), MoveRequest.class);
        return service.addMove(dto.getFrom(), dto.getTo());
    }

    private boolean isEnd(final Request request, final Response response) {
        return service.endGame();
    }

    private Map<PieceColor, Double> getScore(final Request request, final Response response) {
        return service.getScores();
    }

    private PieceColor getTurn(final Request request, final Response response) {
        return service.findTurn();
    }

    private BoardDto getNewBoard(final Request request, final Response response) {
        return new BoardDto(service.restartBoard());
    }

    private String renderInitBoard(Request request, Response response) {
        response.type("text/html");
        return render(new HashMap<>());
    }
}
