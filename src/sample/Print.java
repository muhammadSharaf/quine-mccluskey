package sample;

import Tabular.Implicant;
import Tabular.Table;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class Print {

    public void printColumn(GridPane gridPane, int step, Table table, int size) {
        int rows = 0;
        VBox column = new VBox(2);
        for (int i = 0; i <= size - step; i++) {
            VBox vBox = new VBox(3);
            vBox.setPadding(new Insets(5, 10, 5, 10));
            vBox.getStyleClass().add("implicantGroups");

            StringBuilder titleText = new StringBuilder("G");
            for (int j = 0; j < step; j++) {
                titleText.append("\'");
            }
            titleText.append(i);
            Label title = new Label(titleText.toString());
            title.getStyleClass().add("G");
            vBox.getChildren().add(title);

            Implicant x = table.group[i].head;
            if (table.group[i].size != 0) {
                while (x != null && x.step == step) {
                    StringBuilder implicantText = new StringBuilder();
                    if (x.visited) {
                        implicantText.append("✔\t");
                    } else {
                        implicantText.append(" \t");
                    }
                    implicantText.append(x.value + " ( ");
                    for (Integer dummy : x.dummyDigits) {
                        implicantText.append(dummy + ",");
                    }
                    implicantText.deleteCharAt(implicantText.length() - 1);
                    implicantText.append(" )");
                    Label implicantLabel = new Label(implicantText.toString());
                    implicantLabel.getStyleClass().add("tableImplicants");
                    vBox.getChildren().add(implicantLabel);
                    x = x.next;
                }
            }
            gridPane.add(vBox, step, rows);
            rows++;
        }
    }

}
