package capital.rendering;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.opengl.Texture;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class RenderUtil
{
    public static int vboID;

    public RenderUtil()
    {
        vboID = createID();
    }

    public static void vertexBufferData(FloatBuffer buffer)
    {
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
    }

    public static void indicesBufferData(int id, IntBuffer buffer)
    {
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, id);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
    }


    public static void bindTexture(int id, Texture texture, IntBuffer textureData)
    {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureID());

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, id);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, textureData, GL15.GL_STATIC_DRAW);
    }


    public static void render(int vertexBufferedID)
    {
        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexBufferedID);
        GL11.glVertexPointer(3, GL11.GL_FLOAT, 0, 0);

        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 3);
    }

    public static void renderVAO(int vaoID)
    {
        GL30.glBindVertexArray(vaoID);
        GL20.glEnableVertexAttribArray(0);

        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 3);

        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
    }

    public static void renderLine(int vboID)
    {
        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        GL11.glVertexPointer(3, GL11.GL_FLOAT, 0, 0);

        GL11.glDrawArrays(GL11.GL_LINE, 0, 3);
    }

    public static void render(int vboID, int iboID)
    {
        int vaoID = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoID);

        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);
        GL11.glVertexPointer(3, GL11.GL_FLOAT, 0, 3);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        GL30.glBindVertexArray(0);

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, iboID);
        GL11.glDrawElements(GL11.GL_TRIANGLES, 3, GL11.GL_UNSIGNED_INT, 0);
    }

    public static int createID()
    {
        IntBuffer buffer = BufferUtils.createIntBuffer(1);
        GL15.glGenBuffers(buffer);
        return buffer.get(0);
    }

    public static int createVAOID()
    {
        return GL30.glGenVertexArrays();
    }

    //Utilities function

    /**
     * Must be binned first before using
     *
     * @param pos1
     * @param pos2
     * @param pos3
     * @param pos4
     */
    public static void createQuad(int vertexBufferedID, Vector3f pos1, Vector3f pos2, Vector3f pos3, Vector3f pos4)
    {
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(9);
        FloatBuffer vertexBuffer2 = BufferUtils.createFloatBuffer(9);

        vertexBuffer.put(pos1.x).put(pos1.y).put(pos1.z);
        vertexBuffer.put(pos2.x).put(pos2.y).put(pos2.z);
        vertexBuffer.put(pos3.x).put(pos3.y).put(pos3.z);
        vertexBuffer.flip();

        vertexBuffer2.put(pos1.x).put(pos1.y).put(pos1.z);
        vertexBuffer2.put(pos3.x).put(pos3.y).put(pos3.z);
        vertexBuffer2.put(pos4.x).put(pos4.y).put(pos4.z);
        vertexBuffer2.flip();

        vertexBufferData(vertexBuffer);
        vertexBufferData(vertexBuffer2);

        render(vertexBufferedID);
        render(vertexBufferedID + 1);

        cleanUP();
    }

    /**
     * Use to create floor
     *
     * @param vertexBufferedID  vboID
     * @param startPos          Starting vector
     * @param size              Size of the quad
     */
    public static void createQuad(int vertexBufferedID, Vector3f startPos, float size)
    {
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(9);
        FloatBuffer vertexBuffer2 = BufferUtils.createFloatBuffer(9);

        //Lower-right Triangle
        vertexBuffer.put(startPos.x).put(startPos.y).put(startPos.z);
        vertexBuffer.put(startPos.x + size).put(startPos.y).put(startPos.z);
        vertexBuffer.put(startPos.x + size).put(startPos.y).put(startPos.z + size);
        vertexBuffer.flip();

        //Upper-left Triangle
        vertexBuffer2.put(startPos.x).put(startPos.y).put(startPos.z);
        vertexBuffer2.put(startPos.x + size).put(startPos.y).put(startPos.z + size);
        vertexBuffer2.put(startPos.x).put(startPos.y).put(startPos.z + size);
        vertexBuffer2.flip();

        vertexBufferData(vertexBuffer);
        vertexBufferData(vertexBuffer2);

        render(vertexBufferedID);
        render(vertexBufferedID + 1);

        cleanUP();
    }

    public static void createQuad(int vertexBufferedID, int vertexArrayID, Vector3f startPos, float size)
    {
        GL30.glBindVertexArray(vertexArrayID);

        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(9);
        FloatBuffer vertexBuffer2 = BufferUtils.createFloatBuffer(9);

        //Lower-right Triangle
        vertexBuffer.put(startPos.x).put(startPos.y).put(startPos.z);
        vertexBuffer.put(startPos.x + size).put(startPos.y).put(startPos.z);
        vertexBuffer.put(startPos.x + size).put(startPos.y).put(startPos.z + size);
        vertexBuffer.flip();

        //Upper-left Triangle
        vertexBuffer2.put(startPos.x).put(startPos.y).put(startPos.z);
        vertexBuffer2.put(startPos.x + size).put(startPos.y).put(startPos.z + size);
        vertexBuffer2.put(startPos.x).put(startPos.y).put(startPos.z + size);
        vertexBuffer2.flip();

        vertexBufferData(vertexBuffer);
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);

//        vertexBufferData(vertexBufferedID + 1, vertexBuffer2);
//        GL20.glVertexAttribPointer(1, 3,GL11.GL_FLOAT, false, 0, 0);

        renderVAO(vertexArrayID);

        cleanUP();
    }

    public void drawWorld()
    {
        
    }

    /**
     * Clean UP VBO & VAO
     */
    private static void cleanUP()
    {
        GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL30.glBindVertexArray(0);
    }

    public static void init3D(){
        //Start 3D Stuff
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();

        GLU.gluPerspective(70, Display.getWidth() / Display.getHeight(), 0.3f, 1000);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);
        GL11.glClearDepth(1.0f);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthFunc(GL11.GL_LEQUAL);
        GL11.glFrontFace(GL11.GL_CW);
    }

    public static void clearScreen(){
        //Clear the screen
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glLoadIdentity();
    }
}
