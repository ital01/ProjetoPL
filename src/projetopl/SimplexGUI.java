package projetopl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SimplexGUI extends JFrame {
    private JTextField[][] coeficientes;
    private JButton calcularButton;
    private JTextArea resultadoArea;

    public SimplexGUI() {
        setTitle("Método Simplex");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(3, 5, 10, 10));
        coeficientes = new JTextField[3][5];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 5; j++) {
                coeficientes[i][j] = new JTextField(5);
                inputPanel.add(coeficientes[i][j]);
            }
        }

        JPanel buttonPanel = new JPanel();
        calcularButton = new JButton("Calcular");
        calcularButton.addActionListener(new CalcularListener());
        buttonPanel.add(calcularButton);

        resultadoArea = new JTextArea();
        resultadoArea.setEditable(false);
        resultadoArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(new JScrollPane(resultadoArea), BorderLayout.SOUTH);
    }

    private class CalcularListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                float[][] padronizado = new float[3][5];
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 5; j++) {
                        padronizado[i][j] = Float.parseFloat(coeficientes[i][j].getText());
                    }
                }

                Simplex simplex = new Simplex(2, 4);
                simplex.preencherTabela(padronizado);

                boolean sair = false;
                resultadoArea.append("---Conjunto inicial---\n");
                printTabela(simplex.getTabela());

                while (!sair) {
                    Simplex.ERRO err = simplex.calcular();

                    if (err == Simplex.ERRO.OTIMO) {
                        resultadoArea.append("---Solução Ótima Encontrada---\n");
                        printTabela(simplex.getTabela());
                        sair = true;
                    } else if (err == Simplex.ERRO.INDETERMIANDA) {
                        resultadoArea.append("---Solução é Indeterminada---\n");
                        sair = true;
                    } else {
                        resultadoArea.append("---Iteração---\n");
                        printTabela(simplex.getTabela());
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(SimplexGUI.this, "Por favor, insira números válidos.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void printTabela(float[][] tabela) {
            for (float[] linha : tabela) {
                for (float valor : linha) {
                    resultadoArea.append(String.format("%.2f\t", valor));
                }
                resultadoArea.append("\n");
            }
            resultadoArea.append("\n");
        }
    }
}
