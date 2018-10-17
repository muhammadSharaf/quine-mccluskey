package sample;

import Tabular.*;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

public class Controller {
    public TilePane tile;
    public ScrollPane scroll;
    public ScrollPane scroll1;
    public ScrollPane scroll2;
    public ScrollPane answerScroll;
    public TextField varSizeIn;
    public Button procceed;
    public Button optimize;
    public Button tableImage;
    public Button charImage;
    public Label homeError;
    public int size;
    public int numbers[];
    public ToggleGroup radio[];
    public TabPane tabpane;
    public Tab tab1;
    public Tab tab2;
    public Tab tab3;
    public Tab tab4;
    public Tab tab5;
    public GridPane gridPaneM;
    public GridPane charGrid;
    public Label answerTitle;
    public VBox answers;


    public void procceedButton() {
        try {
            int check = Integer.parseInt(varSizeIn.getText());
            if (Integer.parseInt(varSizeIn.getText()) > 0 && Integer.parseInt(varSizeIn.getText()) < 11) {
                homeError.setText("");
                procceed.setText("Please Wait...");
                tile.getChildren().clear();
                scroll.setContent(tile);
                size = Integer.parseInt(varSizeIn.getText()) + 1;

                radio = new ToggleGroup[(int) Math.pow(2, size - 1)];
                for (int i = 0; i < (int) Math.pow(2, size - 1); i++) {
                    radio[i] = new ToggleGroup();

                    Label label = new Label("m" + i);

                    RadioButton rb1 = new RadioButton("1");
                    rb1.setUserData(1);
                    rb1.setToggleGroup(radio[i]);

                    RadioButton rb2 = new RadioButton("0");
                    rb2.setUserData(0);
                    rb2.setToggleGroup(radio[i]);
                    rb2.setSelected(true);

                    RadioButton rb3 = new RadioButton("X");
                    rb3.setUserData(2);
                    rb3.setToggleGroup(radio[i]);


                    HBox group = new HBox(9);
                    group.getChildren().addAll(rb1, rb2, rb3, label);
                    group.getStyleClass().add("mintermGroup");

                    tile.getChildren().add(group);
                }
                tabpane.getSelectionModel().select(tab2);
                procceed.setText("Minterms");
            } else {
                homeError.setText("Please inter A Valid Variables Number From 1 To 10");
            }
        } catch (Exception e) {
            homeError.setText("Please inter A Valid Variables Number From 1 To 10");
        }
    }

    public void onOptimizeClick() {
        Print print = new Print();
        numbers = new int[(int) Math.pow(2, size - 1)];
        for (int i = 0; i < (int) Math.pow(2, size - 1); i++) {
            numbers[i] = (int) radio[i].getSelectedToggle().getUserData();
        }

        LinkedList<LinkedList<Integer>> prime = new LinkedList<>();
        LinkedList<String> expression = new LinkedList<>();
        LinkedList<Integer> doncaresList = new LinkedList<>();
        LinkedList<Integer> mintermList = new LinkedList<>();
        LinkedList<LinkedList<Integer>> essentials = new LinkedList<>();
        LinkedList<String> essentialExpresion = new LinkedList<>();
        Table tabular = new Table(size);
        Optimize op = new Optimize();

        int numOfZeros = 0;
        int numOfOnes = 0;
        for (int i = 0; i < numbers.length; i++) {
            if (numbers[i] == 1) {
                tabular.setGroup(i, false);
                mintermList.add(i);
                numOfOnes++;
            } else if (numbers[i] == 2) {
                tabular.setGroup(i, true);
                doncaresList.add(i);
            } else {
                numOfZeros++;
            }
        }
        if (checkTrickyInput(numbers.length, numOfOnes, numOfZeros)) {
            numbers = null;
            return;
        }
        numbers = null;

        gridPaneM = new GridPane();
        scroll1.setContent(gridPaneM);
        gridPaneM.prefHeight(250);
        gridPaneM.prefWidth(600);
        gridPaneM.setHgap(5);
        gridPaneM.setVgap(5);
        gridPaneM.setPadding(new Insets(10, 10, 10, 10));

        for (int i = 0; i < size; i++) {
            gridPaneM.getColumnConstraints().add(new ColumnConstraints(150));
        }

        for (int step = 0; step < size; step++) {
            for (int i = 0; i < size - step; i++) {
                tabular.compareGroups(tabular.group[i], tabular.group[i + 1], step, size);

                print.printColumn(gridPaneM, step, tabular, size - 1);

                tabular.addPrime(tabular.group[i], step, prime, expression, size);
                tabular.group[i].removeOldImplicants(step);
            }
        }
        tabpane.getSelectionModel().select(tab3);

        //char..
        charGrid = new GridPane();
        charGrid.setGridLinesVisible(true);
        charGrid.setPadding(new Insets(5, 5, 5, 5));
        scroll2.setContent(charGrid);
        tabular.removeDoncaresFromPrime(doncaresList, prime);

        //making minterm row
        boolean firstVisit = true;
        for (int i = 0; i < mintermList.size(); i++) {
            if (firstVisit) {
                charGrid.getColumnConstraints().add(new ColumnConstraints(120));
                i--;
                firstVisit = false;
                continue;
            }
            charGrid.getColumnConstraints().add(new ColumnConstraints(50));
            Label minterm = new Label();
            minterm.getStyleClass().add("Minterm");
            charGrid.setHalignment(minterm, HPos.CENTER);
            minterm.setText("m" + mintermList.get(i).toString());
            charGrid.add(minterm, i + 1, 0);
        }

        //filling essential primes rows
        int i = 0;
        int columnIndex;
        LinkedList<Integer> rowRemoved = new LinkedList<>();

        op.getEssential(prime, essentials, essentialExpresion, expression, rowRemoved);

        for (; i < essentials.size(); i++) {
            charGrid.getRowConstraints().add(new RowConstraints(30));
            Label primeExpression = new Label(essentialExpresion.get(i));
            primeExpression.getStyleClass().add("PrimeExpression");
            charGrid.setHalignment(primeExpression, HPos.CENTER);
            charGrid.add(primeExpression, 0, i + 1);
            for (Integer minterm : essentials.get(i)) {
                columnIndex = mintermList.indexOf(minterm) + 1;
                Label x = new Label("X");
                if (rowRemoved.contains(minterm)) {
                    x.getStyleClass().add("essPrime");
                } else {
                    x.getStyleClass().add("essX");
                }
                charGrid.add(x, columnIndex, i + 1);
                charGrid.setHalignment(x, HPos.CENTER);
            }
        }

        //row dom
        LinkedList<LinkedList<Integer>> removedRows = new LinkedList<>();
        LinkedList<String> removedRowsExpression = new LinkedList<>();
        op.rowDom(prime, expression, removedRows, removedRowsExpression);
        for (; i < removedRows.size(); i++) {
            charGrid.getRowConstraints().add(new RowConstraints(30));
            Label rowExpression = new Label(removedRowsExpression.get(i));
            rowExpression.getStyleClass().add("removedRows");
            charGrid.setHalignment(rowExpression, HPos.CENTER);
            charGrid.add(rowExpression, 0, i + 1);
            for (Integer minterm : removedRows.get(i)) {
                columnIndex = mintermList.indexOf(minterm) + 1;
                Label x = new Label("X");
                x.getStyleClass().add("rowX");
                charGrid.add(x, columnIndex, i + 1);
                charGrid.setHalignment(x, HPos.CENTER);
            }
        }

        //ColumnDom
        LinkedList<Integer> removedColumn = new LinkedList<>();
        LinkedList<String>[] charArray = new LinkedList[(int) Math.pow(2, size - 1)];
        for (int k = 0; k < (int) Math.pow(2, size - 1); k++) {
            LinkedList<String> list = new LinkedList<>();
            charArray[k] = list;
        }

        op.mintermArray(charArray, prime, expression);
        op.columnDom(charArray, removedColumn);

        for (int k = 0; k < prime.size(); k++) {
            charGrid.getRowConstraints().add(new RowConstraints(30));
            Label remainingPrime = new Label(expression.get(k));
            remainingPrime.getStyleClass().add("remainingPrime");
            charGrid.setHalignment(remainingPrime, HPos.CENTER);
            charGrid.add(remainingPrime, 0, i + 1);
            for (Integer minterm : prime.get(k)) {
                columnIndex = mintermList.indexOf(minterm) + 1;
                Label x = new Label("X");
                if (removedColumn.contains(minterm)) {
                    x.getStyleClass().add("columnX");
                } else {
                    x.getStyleClass().add("Patrik");
                }
                charGrid.add(x, columnIndex, i + 1);
                charGrid.setHalignment(x, HPos.CENTER);
            }
            i++;
        }

        // patrik
        LinkedList<LinkedList<String>> allPossibles = new LinkedList<>();
        LinkedList<String> temp = new LinkedList<String>();
        LinkedList<String> finalResult = new LinkedList<>();
        String essentialExpression = op.fromListToString(essentialExpresion);
        int firstMinterm = 0;
        while (firstMinterm < charArray.length && charArray[firstMinterm].isEmpty()) {
            firstMinterm++;
        }
        if (firstMinterm < charArray.length) {
            op.patrik(charArray, firstMinterm, charArray.length, allPossibles, temp);
        }

        op.removeDublicatesAfterPatrick(allPossibles);
        op.getMinimizedForm(allPossibles, essentialExpression, finalResult);

        answers.getChildren().clear();
        answerScroll.setContent(answers);

        for (String s : finalResult) {
            Label answer = new Label(s);
            answer.getStyleClass().add("Answer");
            answers.getChildren().add(answer);
            System.out.println(s);
        }
    }

    public void onTableImageClick() {
        WritableImage image = gridPaneM.snapshot(new SnapshotParameters(), null);
        File file = new File("table.png");
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        } catch (IOException e) {

        }
        File f = new File("table.png");
        Desktop dt = Desktop.getDesktop();
        try {
            dt.open(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onCharImageClick() {
        WritableImage image = charGrid.snapshot(new SnapshotParameters(), null);
        File file = new File("char.png");
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        } catch (IOException e) {

        }
        File f = new File("char.png");
        Desktop dt = Desktop.getDesktop();
        try {
            dt.open(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean checkTrickyInput(int size, int numOfOnes, int numOfZeros) {
        boolean flag = false;
        if (numOfOnes > 0 && numOfZeros == 0) {
            massage("Tricky", "This Function Is 1");
            flag = true;
        } else if (numOfZeros > 0 && numOfOnes == 0) {
            massage("Tricky", "This Function Is 0");
            System.exit(0);
            flag = true;
        } else if ((numOfOnes == 0 && numOfZeros == 0) || (numOfZeros < size && numOfOnes == 0)) {
            massage("Tricky", "A Dontcare Function Can Be 0 Or 1");
            flag = true;
        }
        return flag;
    }

    public void massage(String title, String massage) {
        Stage window = new Stage();
        Label contenet = new Label(massage);
        Button ok = new Button("OK");
        ok.setOnAction(event -> {
            window.close();
        });

        VBox mainLayout = new VBox(10);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.getChildren().addAll(contenet, ok);

        Scene scene = new Scene(mainLayout, 300, 80);
        window.setScene(scene);
        window.initModality(Modality.APPLICATION_MODAL);
        window.showAndWait();
    }
}
