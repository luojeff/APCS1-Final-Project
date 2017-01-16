import javax.swing.text.*;
import javax.swing.text.AbstractDocument.*;

public class HtmlSyntax {

    public static class Token {
        public int start;
        public int end;
        public String name;

        public Token(String n, int s, int e) {
            name = n;
            start = s;
            end = e;
        }

        public Token(Element e) {
            return new Token(getTokenName(e), e.getStartOffset(), e.getEndOffset());
        }

        public static String getTokenName(Element e) {
            if(e.getName().equals("content")) {return "";}
            return e.getName();
        }

        public int length() {
            return end - start;
        }

        public String toString() {
            return name + "[" + start + ":" + end + "]";
        }

        public boolean equals(Token other) {
            return (other.name.equals(this.name) && other.start == this.start && other.end == this.end);
        }

        public Token copy() {
            return new Token(name, start, end);
        }
    }
}