package org.example.controladores;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.javalin.Javalin;
import org.example.encapsulaciones.Usuario;
import org.example.encapsulaciones.Visitante;
import org.example.servicios.UsuarioServices;
import org.example.servicios.mongo.UsuarioODM;
import org.example.servicios.mongo.VisitanteODM;
import org.example.utils.ControladorClass;
import org.example.utils.JWTutils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class IndexControlador extends ControladorClass {

	public IndexControlador(Javalin app){
		super(app);
	}

	@Override
	public void aplicarRutas() {

		app.get("/", context -> {

			String jwt = context.cookie("jwt");
			if (jwt != null && UsuarioServices.getInstancia().getUsuarioLogueado() == null){
				Claim username = JWTutils.decodeJWT(jwt).getClaim("username");
				UsuarioServices.getInstancia().setUsuarioLogueado(UsuarioODM.getInstance().buscarUsuarioByUsername(username.asString()));
			} else {
				Visitante visitante;
				if (context.cookie("visitorID") == null){
					visitante = new Visitante(UUID.randomUUID().toString());
					context.cookie("visitorID", visitante.getId());
					VisitanteODM.getInstance().guardarVisitante(visitante);
				}else {
					visitante = new Visitante();
				}
				UsuarioServices.getInstancia().setVisitanteActual(visitante);
			}

			Usuario usuario = UsuarioServices.getInstancia().getUsuarioLogueado();
			Map<String,Object> model = new HashMap<>();
			model.put("usuario", usuario);

			context.render("publico/html/index.html", model);
		});
	}
}
