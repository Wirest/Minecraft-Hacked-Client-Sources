#ifdef GL_ES
precision mediump float;
#endif

#extension GL_OES_standard_derivatives : enable

uniform sampler2D diffuseSamper;
uniform vec2 texel;
uniform vec2 resolution;
uniform float timeHelper;

float time = timeHelper;

//END


#define PI 3.14159265359
#define T (time/0.4)

vec3 hsv2rgb(vec3 c)
{
    vec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);
    vec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);
    return c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);
}

void main( void ) {

    vec2 position = (( gl_FragCoord.xy / resolution.xy ) - 0.5);
    position.x *= resolution.x / resolution.y;
    position.y -= 1.0;
    
    vec3 color = vec3(0.);
    vec4 centerCol = texture2D(diffuseSamper, gl_TexCoord[0].st);
    
    
    for (float i = 0.; i < PI*1.0; i += PI/20.0) {
        vec2 p = position - vec2(cos(i+T), sin(i+T)) * 0.15;
        vec3 col = hsv2rgb(vec3((i)/(PI*2.0), 1., mod(i-T*3.,PI*2.)/PI));
        color += col * (1./512.) / length(p);
    }

    gl_FragColor = vec4( color, 1.0 );

}