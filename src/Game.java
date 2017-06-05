import javafx.scene.canvas.GraphicsContext;

import java.io.*;

class Game implements Serializable {
    private int[][] world;

    Game(File map) {
        world = new int[16][16];
        loadMap(map);
    }

    private void loadMap(File file) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            String line = br.readLine();
            for (int i = 0; line != null; i++, line = br.readLine()) {
                String[] tmp = line.split("/");
                for (int j = 0; j < tmp.length; j++)
                    world[i][j] = Integer.valueOf(tmp[j]);
            }
        } catch (IOException e) {
            System.out.println(e.toString());
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                System.out.println(e.toString());
            }
        }
    }

    public int[][] getWorld() {
        return world;
    }
}
