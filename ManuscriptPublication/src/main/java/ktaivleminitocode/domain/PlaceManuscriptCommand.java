package ktaivleminitocode.domain;


import lombok.Data;

@Data
public class PlaceManuscriptCommand {

    private Long authorId;
    private String title;
    private String content;
}
