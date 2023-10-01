precision mediump float;

uniform sampler2D u_TextureUnit;
uniform float scroll;

varying vec2 v_TextureCoordinates;

void main() {
    gl_FragColor = texture2D(u_TextureUnit, vec2(v_TextureCoordinates.x - scroll, v_TextureCoordinates.y));
}
