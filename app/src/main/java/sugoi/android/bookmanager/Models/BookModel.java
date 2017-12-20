package sugoi.android.bookmanager.Models;

/**
 * Created by Adil on 16-12-2017.
 */

public class BookModel {
    public int getB_id() {
        return b_id;
    }

    public void setB_id(int b_id) {
        this.b_id = b_id;
    }

    private int b_id;
    private String  book_name;
    private String author;
    private String type;
    private String lastseen;
    private String owner;

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }



    public BookModel(int b_id,String book_name, String author, String type,String owner, String lastseen) {
        this.book_name = book_name;
        this.author = author;
        this.type = type;
        this.lastseen = lastseen;
        this.b_id = b_id;
        this.owner = owner;
    }



    public String getBook_name() {
        return book_name;
    }

    public void setBook_name(String book_name) {
        this.book_name = book_name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLastseen() {
        return lastseen;
    }

    public void setLastseen(String lastseen) {
        this.lastseen = lastseen;
    }


}
