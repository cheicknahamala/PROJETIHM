package sample;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class Main extends Application{

    private static final int X = 9;
    private static final int Y = 9;

    private Carreau[][] grille = new Carreau[9][9];
    private Scene scene;

    private static final Image[] digits = new Image[9];

    private Parent createContent() {
        Pane root = new Pane();
        root.setPrefSize(360, 360);


        for (int y = 0; y < Y; y++) {
            for (int x = 0; x < X; x++) {
                Carreau carre = new Carreau(x, y, Math.random() < 0.2);

                grille[x][y] = carre;


                root.getChildren().add(carre);

            }

        }


        for (int y = 0; y < Y; y++) {
            for (int x = 0; x < X; x++) {
                Carreau carre = grille[x][y];


                if (carre.hasBomb)
                    continue;

                long bombs = getNeighbors(carre).stream().filter(t -> t.hasBomb).count();

                if (bombs > 0) {
                    carre.text.setText(String.valueOf(bombs));
                    for (int i = 0; i < digits.length; i++) {
                        Image digits[] = {new Image("images/1.png"), new Image("images/2.png"), new Image("images/3.png"), new Image("images/4.png"), new Image("images/5.png"), new Image("images/6.png"), new Image("images/7.png"), new Image("images/8.png"), new Image("images/9.png")};
                        if (bombs == i + 1) {
                            //System.out.println("true");


                            carre.border.setFill(new ImagePattern(digits[i]));
                            carre.border.setStroke(Color.LIGHTGRAY);
                            //carre.border.setFill(Color.DARKGRAY);



                        }

                    }

                }
            }
        }

        return root;
    }

    private List<Carreau> getNeighbors(Carreau carre) {
        List<Carreau> voisins = new ArrayList<>();

        int[] points = new int[]{
                -1, -1,
                -1, 0,
                -1, 1,
                0, -1,
                0, 1,
                1, -1,
                1, 0,
                1, 1
        };

        for (int i = 0; i < points.length; i++) {
            int dx = points[i];
            int dy = points[++i];

            int newX = carre.x + dx;
            int newY = carre.y + dy;

            if (newX >= 0 && newX < X
                    && newY >= 0 && newY < Y) {
                voisins.add(grille[newX][newY]);
            }
        }

        return voisins;
    }

    private class Carreau extends StackPane{
        private int x, y;
        private boolean hasBomb;
        private boolean isOpen = false;

        private Rectangle border = new Rectangle(38, 38);
        private Text text = new Text();

        public Carreau(int x, int y, boolean hasBomb) {
            this.x = x;
            this.y = y;
            this.hasBomb = hasBomb;
            Image image = new Image("images/mine.png");
            Image im = new Image("images/flag.png");

            text.setFont(Font.font(40));
            border.setStroke(Color.LIGHTGRAY);
            border.setFill(Color.DARKGRAY);
            text.setVisible(false);
            border.setVisible(true);




            text.setText(hasBomb ? "X" : "");


            getChildren().addAll(border, text);

            setTranslateX(x * 40);
            setTranslateY(y * 40);

            setOnMouseClicked(e -> open());

        }

        public void open() {
            if (isOpen)
                return;

            if (hasBomb) {


                System.out.println("Perdu");

                scene.setRoot(createContent());
                return;
            }

            isOpen = true;
            text.setVisible(false);
            border.setVisible(true);



            if (text.getText().isEmpty()) {
                getNeighbors(this).forEach(Carreau::open);
                this.border.setFill(Color.WHITE);
            }

        }


    }


        @Override
        public void start(Stage stage) throws Exception {
            scene = new Scene(createContent());
            stage.setTitle("Demineur");
            stage.setScene(scene);
            stage.show();
        }





        public static void main(String[] args) {
            launch(args);
        }

}






