#version 120

uniform sampler2D diffuseSamper;
uniform vec2 texel;
uniform vec2 resolution;
uniform float timeHelper;

float time = timeHelper;

//END

bool onPlayer(vec4 color){
return color.r != 0.0F && color.g != 0.0F && color.b != 0.0F;
}

void main( void ) {

float radius = 1.1;

 vec4 centerCol = texture2D(diffuseSamper, gl_TexCoord[0].st);
 if(centerCol.a != 0.0F) return;

  for(float x = -radius; x <= radius; x++){
   for(float y = -radius; y <= radius; y++){
       vec4 currentColor = texture2D(diffuseSamper, gl_TexCoord[0].st + vec2(texel.x * x, texel.y * y));
       if(currentColor.a != 0.0F){
         gl_FragColor = vec4(1, 0, 0, 1);
       }
    }
  }
}