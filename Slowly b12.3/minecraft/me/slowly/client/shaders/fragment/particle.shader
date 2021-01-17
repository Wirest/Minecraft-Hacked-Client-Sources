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

const float aoinParam1 = 0.7;
float snow(vec2 uv,float scale)
{
    float w=smoothstep(9.,0.,-uv.y*(scale/10.));if(w<.1)return 0.;
    uv+=(time*aoinParam1)/scale;uv.y+=time*0./scale;uv.x+=sin(uv.y+time*.05)/scale;
    uv*=scale;vec2 s=floor(uv),f=fract(uv),p;float k=3.,d;
    p=.5+.35*sin(11.*fract(sin((s+p+scale)*mat2(7,3,6,5))*5.))-f;d=length(p);k=min(d,k);
    k=smoothstep(0.,k,sin(f.x+f.y)*0.01);
        return k*w;
}

void main(void){
    vec4 centerCol = texture2D(diffuseSamper, gl_TexCoord[0].st);
    vec2 uv=(gl_FragCoord.xy*2.-resolution.xy)/min(resolution.x,resolution.y); 
    vec3 finalColor=vec3(centerCol.r, centerCol.g, centerCol.b);
    float c=smoothstep(1.,0.3,clamp(uv.y*.3+.8,0.,.75));
    float old = c;
    c+=snow(uv,30.)*.3;
    c+=snow(uv,20.)*.5;
    c+=snow(uv,15.)*.8;
    c+=snow(uv,10.);
    c+=snow(uv,8.);
    c+=snow(uv,6.);
    c+=snow(uv,5.);
    finalColor=(vec3(c));
    gl_FragColor = vec4(centerCol);
    if(c != old){
    gl_FragColor = vec4(c + centerCol);
    }
}