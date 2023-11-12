package es.florida.multifil.avaluable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Order extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel panellContingut;
    private JTextField txtTipus;
    private JTextField txtQuantitat;
    private Manufacture fabricacio;

    // Conjunt de tipus de peces vàlids
    private Set<String> tipusPecesValids = new HashSet<>(Arrays.asList("I", "O", "T", "J", "L", "S", "Z"));

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Order frame = new Order();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Order() {
        fabricacio = new Manufacture(); // Creem una instància de Manufacture

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        panellContingut = new JPanel();
        panellContingut.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(panellContingut);
        panellContingut.setLayout(null);

        JLabel lblQuantitat = new JLabel("Quantitat");
        lblQuantitat.setBounds(10, 108, 62, 19);
        lblQuantitat.setFont(new Font("Tahoma", Font.PLAIN, 15));
        panellContingut.add(lblQuantitat);

        JLabel lblTipus = new JLabel("Tipus de Peça");
        lblTipus.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblTipus.setBounds(10, 61, 99, 19);
        panellContingut.add(lblTipus);

        txtTipus = new JTextField();
        txtTipus.setBounds(119, 63, 96, 19);
        panellContingut.add(txtTipus);
        txtTipus.setColumns(10);

        txtQuantitat = new JTextField();
        txtQuantitat.setColumns(10);
        txtQuantitat.setBounds(119, 110, 96, 19);
        panellContingut.add(txtQuantitat);

        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.setBounds(308, 199, 85, 21);
        panellContingut.add(btnGuardar);

        btnGuardar.addActionListener(e -> {
            if (validarCamps()) {
                realitzarComanda();
            } else {
                // Mostra un missatge d'error a l'usuari
                JOptionPane.showMessageDialog(this, "Completeu tots els camps correctament abans de guardar.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    /**
     * Realitza una comanda segons els valors introduïts per l'usuari.
     */
    private void realitzarComanda() {
        String[] tipusPeces = txtTipus.getText().split(",");
        String[] quantitats = txtQuantitat.getText().split(",");

        if (tipusPeces.length != quantitats.length) {
            // Mostra un missatge d'error a l'usuari
            JOptionPane.showMessageDialog(this, "Assegurat que hi haja una quantitat per a cada tipus de peça.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Set<String> tipusUnics = new HashSet<>(Arrays.asList(tipusPeces));

        if (tipusUnics.size() != tipusPeces.length) {
            // Mostra un missatge d'error a l'usuari
            JOptionPane.showMessageDialog(this, "No es permeten tipus de peça duplicats.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Map<String, Integer> comandaMapa = new HashMap<>();

        for (int i = 0; i < tipusPeces.length; i++) {
            String tipus = tipusPeces[i].trim();
            int quantitat = Integer.parseInt(quantitats[i].trim());

            if (!tipusPecesValids.contains(tipus)) {
                JOptionPane.showMessageDialog(this, "Tipus de peça desconegut: " + tipus, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (quantitat <= 0) {
                JOptionPane.showMessageDialog(this, "La quantitat ha de ser major que zero.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            comandaMapa.put(tipus, quantitat);
        }

        fabricacio.iniciarProduccio(comandaMapa);
    }

	/**
	 * Valida que els camps de la comanda estiguin omplerts i que la quantitat no
	 * siga zero.
	 *
	 * @return True si els camps són vàlids, Fals si no.
	 */
	private boolean validarCamps() {
		String quantitatText = txtQuantitat.getText().trim();

		// Verifica que els camps no estiguen buits i la quantitat no siga zero
		return !txtTipus.getText().isEmpty() && !quantitatText.isEmpty() && !quantitatText.equals("0");
	}
}